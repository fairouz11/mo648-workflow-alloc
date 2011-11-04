package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

class HostImpl implements br.unicamp.ic.wfscheduler.Host
{	
	private static final int ramDefault = 2048;
	private static final long storageDefault = 1000000;
	
	private static int hostID = 0;
	private static int peID = 0;
	private static int vmID = 0;
	
	private Host host;
	private Vm vm;
	
	private BrokerImpl broker;
	
	/**
	 * Keeps an array of tasks that will be transmitted to hosts.
	 */
	private ArrayList<Transmission> transmissionList;
	/**
	 * Current transmissions
	 */
	private Hashtable<HostImpl, Transmission> currentTransmission;
	
	/**
	 * Available results on this host - <task, arrivedTime>.
	 */
	private Hashtable<TaskImpl, Double> availableResults;
	
	
	public HostImpl(long mips, int processorCount, BrokerImpl broker)
	{		
		if (processorCount < 1)
			throw new RuntimeException("Invalid processorCount");
		
		this.broker = broker;
		this.transmissionList = new ArrayList<Transmission>();
		this.currentTransmission = new Hashtable<HostImpl, Transmission>();
		this.availableResults = new Hashtable<TaskImpl, Double>();
		
		List<Pe> peList = new ArrayList<Pe>((int)processorCount);
		
		for (int i = 0; i < processorCount; i++)
			peList.add(new Pe(peID++, new PeProvisionerSimple(mips)));
		
		host = new Host(hostID++, new RamProvisionerSimple(ramDefault), 
				new BwProvisionerSimple(broker.getBandwidth()),
				storageDefault, peList, new VmSchedulerTimeShared(peList));
		
		vm = new Vm(vmID++, broker.getUserID(), mips, processorCount, 
				ramDefault, broker.getBandwidth(), storageDefault, "vm",
				new CloudletDependencyScheduler(this, broker));
	}
	
	/**
	 * Get cloud sim host.
	 * @return
	 */
	Host getCsHost()
	{
		return host;		
	}
	
	/**
	 * Get cloudsim vm.
	 * @return
	 */
	Vm getCsVm()
	{
		return vm;		
	}
	
	/**
	 * Get broker
	 * @return broker
	 */
	BrokerImpl getBroker()
	{
		return broker;
	}
	
	/**
	 * Queue a task to be transmitted to destination host when finished
	 * @param task to be transmitted
	 * @param destintation host
	 */
	void transmit(TaskImpl task, HostImpl destintation)
	{
		Transmission t;
		
		t = new Transmission(destintation, task);
		
		transmissionList.add(t);
	}
	
	/**
	 * Calls updateTransmission and register new event
	 */
	void updateTransmissionAndYell()
	{
		Datacenter dc = (Datacenter)getCsHost().getDatacenter();
    	double nextEvent = Double.MAX_VALUE;
    	
    	nextEvent = Math.min(Double.MAX_VALUE, updateTransmission());
    	
    	if (nextEvent > 0 && nextEvent != Double.MAX_VALUE)
			dc.schedule(dc.getId(), nextEvent - CloudSim.clock(), dc.TRANSMISSION_EVENT);
	}
	
	/**
	 * Update transmissions
	 * @return time when next transmission will be ready or 0 if no more transmissions
	 */
	double updateTransmission()
	{	
		double nextEvent;
		double currentTime = CloudSim.clock();
		
		// finish transmissions
		for (HostImpl host : currentTransmission.keySet())
		{
			Transmission t = currentTransmission.get(host);

			// if current transmission is not null for destination host
			if (t != null)
			{
				// let's see if it's finished
				if (t.getFinishTime() <= currentTime)
				{
					// it's finished, let's free the transmission channel
					currentTransmission.remove(host);
					t.finishTransmission();
					
					Log.printLine(CloudSim.clock()+": " +
						"Host #" + this.host.getId()+ ": Transmission finished: Task #"+
							t.getTask().getCsCloudlet().getCloudletId()+" to Host #"+host.getCsHost().getId()+".");					
				}	
			}
		}
		
		if (transmissionList.size() == 0)
			nextEvent = 0;
		else
			nextEvent = Double.MAX_VALUE;
		
		// for each new transmission
		for (int i = 0; i < transmissionList.size(); i++)
		{
			Transmission t = transmissionList.get(i);
			HostImpl destintation = t.getHost();
			Transmission currTrans = currentTransmission.get(destintation); 
			
			// we can only transmit tasks that've finished
			if (t.getTask().hasFinished())
			{			
				// if we can transmit
				if (currentTransmission.get(destintation) == null)
				{
					
					Log.printLine(CloudSim.clock()+": " +
							"Host #" + this.host.getId()+ ": Transmission began: Task #"+
								t.getTask().getCsCloudlet().getCloudletId()+" to Host #"+t.getHost().getCsHost().getId()+".");					
					
					t.beginTransmission();
					currentTransmission.put(destintation, t);
	
					nextEvent = Math.min(nextEvent, t.getFinishTime());
					
					// ok, we've began transmitting, let's remove it from the transmission list
					transmissionList.remove(i);
					i--; // ajust the counter
				}
			}
			else
			{
				// task's not finished, let's not create a new event, let's wait for
				// the cloudlet scheduler tell us

				//------------------------------------------------------
				// ignore this
				// task was not finished, let's wait until it finishes
				// we estimate the finish time by it's length
				// TODO improve estimation based on execution queue
				//nextEvent = Math.min(nextEvent, CloudSim.clock() + t.getTask().getProcessingTime(this));	
			}
		}
		
		return nextEvent;
	}
	
	/**
	 * Get the time when a result will be available to this host
	 * @param task task whose result you want
	 * @return 0 if result is already available. negative if its a prediction (and you should query again when time comes). positive
	 * otherwise
	 */
	double getResultReadyTime(TaskImpl task)
	{
		Double arrivedTime = availableResults.get(task); 

		// I have the result right here!
		if (arrivedTime != null)
			return arrivedTime.doubleValue();
		
		// get the host which was assigned to this task
		HostImpl sender = broker.getAssignedHost(task);
		
		// is task finished execution?
		if (task.hasFinished())
		{
			// yes, it is
			// so it should be being transmitted or be in the transmission list
			Transmission t = sender.currentTransmission.get(this);
			
			if (t != null)
			{
				// is the task being transmitted to me the one I want now?
				if (t.getTask() == task)
				{
					// yes, we know for sure when it'll arrive
					return t.getFinishTime();
				}
				else
				{
					// no, that's not it, let's just give an estimation
					return -t.getFinishTime();
				}
			}
			else
			{
				// it's on transmission queue
				t = sender.findTransmissionOnQueue(task, this);
				
				if (t == null)
					throw new Error("Invalid transmission state!");
				
				// just and estimation
				return -t.estinateFinishTime();
			}
		}
		else
		{
			double taskProcessingStartTime = task.getProcessingStartTime();
			
			taskProcessingStartTime = taskProcessingStartTime == -1 ? CloudSim.clock() : taskProcessingStartTime; 
			
			// task is not finished
			// we will estimate transmission time based on the time the current task would take
			// if it was sent now plus its remaining processing time
			return -(taskProcessingStartTime + task.getTransmissionTime() + task.getCurrentFinishTimeEstimation());	
		}
		
		// no execution flow gets here
	}
	
	/**
	 * Informs this host that a task's result has arrived
	 * @param task task whose results have arrived 
	 * @param arrivedTime time when results have arrived
	 */
	void addTaskResult(TaskImpl task, double arrivedTime)
	{
		availableResults.put(task, arrivedTime);
	}
	
	/**
	 * Find a transmission on current transmission queue
	 * @param task
	 * @param destination
	 * @return
	 */
	Transmission findTransmissionOnQueue(TaskImpl task, HostImpl destination)
	{
		for (Transmission t : transmissionList)
			if (t.getHost() == destination && t.getTask() == task)
				return t;
				
		return null;
	}
	
	/**
	 * Returns whether a task is on transmission queue
	 * @param task
	 * @return
	 */
	boolean isTaskOnTransmissionQueue(TaskImpl task)
	{
		for (Transmission t : transmissionList)
			if (t.getTask() == task)
				return true;
				
		return false;
	}
	
	@Override
	public long getProcessingSpeed()
	{
		return host.getTotalMips();
	}

	@Override
	public double getCost()
	{
		return broker.getProcessingCost();
	}

	@Override
	public long getProcessorCount()
	{
		return host.getPesNumber();
	}

}
