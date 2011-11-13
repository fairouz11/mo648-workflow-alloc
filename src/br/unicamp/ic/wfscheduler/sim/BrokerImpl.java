package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.jws.Oneway;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.core.CloudSim;

import br.unicamp.ic.wfscheduler.Broker;
import br.unicamp.ic.wfscheduler.Host;
import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.Task;

class BrokerImpl implements Broker
{	
	private static final int costPerMem = 0;
	private static final int costPerStorage = 0;
	private static final int costPerBw = 0;
	
	/**
	 * true when simulation has started
	 */
	private boolean simulationOnline;
	
	private boolean newTask;
	private boolean newTransmission;
	
	private long bandwidth;
	private double processingCost;
	private Datacenter datacenter;
	private DatacenterCharacteristics caracteristics;
	private DatacenterBroker dcBroker;	
	
	private ArrayList<HostImpl> hosts;
	private ArrayList<TaskImpl> tasks;
	private Hashtable<Cloudlet, TaskImpl> cloudletMapping;
	private Hashtable<org.cloudbus.cloudsim.Host, HostImpl> hostMapping;
	private Hashtable<TaskImpl, HostImpl> allocation;
	private Hashtable<TaskImpl, ArrayList<HostImpl>> transmission;
	
	private IScheduler scheduler;
	
	public BrokerImpl(List<br.unicamp.ic.wfscheduler.sim.input.Host> hosts,
			List<br.unicamp.ic.wfscheduler.sim.input.Task> tasks,
			long bandwidth, double processingCost,
			IScheduler scheduler) throws Exception
	{
		Hashtable<br.unicamp.ic.wfscheduler.sim.input.Task, TaskImpl> taskMapping = 
				new Hashtable<br.unicamp.ic.wfscheduler.sim.input.Task, TaskImpl>(tasks.size());
		ArrayList<org.cloudbus.cloudsim.Host> hostList = new ArrayList<org.cloudbus.cloudsim.Host>(hosts.size());
		ArrayList<Vm> vmList = new ArrayList<Vm>(hosts.size());
		ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>(tasks.size());
		
		this.simulationOnline = false;
		
		this.hostMapping = new Hashtable<org.cloudbus.cloudsim.Host, HostImpl>(hosts.size());
		this.cloudletMapping = new Hashtable<Cloudlet, TaskImpl>(tasks.size());
		this.allocation = new Hashtable<TaskImpl, HostImpl>(tasks.size());
		this.transmission = new Hashtable<TaskImpl, ArrayList<HostImpl>>(tasks.size());
		this.hosts = new ArrayList<HostImpl>(hosts.size());
		this.scheduler = scheduler;
		this.tasks = new ArrayList<TaskImpl>(tasks.size());
		this.bandwidth = bandwidth;
		this.processingCost = processingCost;
		this.dcBroker = new DatacenterBroker(this);
		this.caracteristics = new DatacenterCharacteristics("X86", "linux", "Xen", 
				hostList, 10, processingCost, costPerMem, costPerStorage, costPerBw);
		
		
		for (br.unicamp.ic.wfscheduler.sim.input.Task t : tasks)
		{		
			TaskImpl ti = new TaskImpl(t.getLength(), t.getOutputSize(), this);
			cloudletList.add(ti.getCsCloudlet());
			this.tasks.add(ti);
			
			this.cloudletMapping.put(ti.getCsCloudlet(), ti);
			
			taskMapping.put(t, ti);
		}
		
		// task dependencies
		for (br.unicamp.ic.wfscheduler.sim.input.Task t : tasks)
		{
			TaskImpl ti = taskMapping.get(t);
			
			for (br.unicamp.ic.wfscheduler.sim.input.Task td : t.getDependencies())
			{
				ti.addDependency(taskMapping.get(td));
			}
		}
		
		for (br.unicamp.ic.wfscheduler.sim.input.Host h : hosts)
		{
			HostImpl hi = new HostImpl(h.getMips(), h.getProcessorCount(), this);
			vmList.add(hi.getCsVm());
			this.hosts.add(hi);
			this.hostMapping.put(hi.getCsHost(), hi);
			hostList.add(hi.getCsHost());
		}
		
		datacenter = new Datacenter("datacenter", caracteristics, new VmAllocationPolicySimple(hostList),
				new LinkedList<Storage>(), 0, this);
		
		dcBroker.submitVmList(vmList);
		dcBroker.submitCloudletList(cloudletList);		
	}
	
	void start()
	{
		scheduler.startScheduler(this);
		
		simulationOnline = true;
		CloudSim.startSimulation();
		
		CloudSim.stopSimulation();
	}
	
	int getUserID()
	{
		return dcBroker.getId();
	}

	double getProcessingCost()
	{
		return processingCost;
	}
	
	/**
	 * Get total execution cost
	 * @return
	 */
	double getTotalCost()
	{
		double totalCost = 0;
		
		for (TaskImpl t : tasks)
			totalCost += t.calculateProcessingCost(allocation.get(t));
		
		return totalCost;
	}
	
	/**
	 * Get total elapsed time
	 * @return
	 */
	double getTotalTime()
	{
		// searches for last executted task
		double last = 0;
		
		for (TaskImpl t : tasks)
		{
			double ft = t.getCsCloudlet().getFinishTime();
			
			if ( ft > last )
				last = ft;
		}
		
		// TODO consider transmission time
		
		return last;
	}
	
	/**
	 * Get host assigned to work on this task
	 * @param task to query host for
	 * @return host assigned to work on the task
	 */
	HostImpl getAssignedHost(TaskImpl task)
	{
		HostImpl h = allocation.get(task);
		
		if (h == null)
			throw new Error("Task #"+ task.getCsCloudlet().getCloudletId() +" was not assigned.");
		
		return h;
	}
	
	/**
	 * Find a task based on its cloudlet
	 * @param cloudlet to search for
	 * @return task that has cloudlet
	 */
	TaskImpl getTask(Cloudlet cloudlet)
	{
		TaskImpl t;
		
		t = cloudletMapping.get(cloudlet);
		
		if (t == null)
			throw new Error("Couldn't find task!");
		
		return t;
	}
	
	/**
	 * Find a host based on its cloudsim host
	 * @param host to search for
	 * @return host that has cloudsim host
	 */
	HostImpl getHost(org.cloudbus.cloudsim.Host host)
	{
		HostImpl h;
		
		h = hostMapping.get(host);
		
		if (h == null)
			throw new Error("Couldn't find host!");
		
		return h;
	}
	
	@Override
	public long getBandwidth()
	{
		return bandwidth;
	}

	@Override
	public List<Task> getTasks()
	{
		return new ArrayList<Task>(tasks);
	}
	
	List<TaskImpl> getInternalTasks()
	{
		return tasks;
	}

	@Override
	public List<Host> getHosts()
	{
		return new ArrayList<Host>(hosts);
	}
	
	List<HostImpl> getInternalHosts()
	{
		return hosts;
	}
	
	private void handleOnlineSchedulling()
	{
		if (!simulationOnline) 
			return;
	
		// checks if scheduler did anything that needs to be simulated
		if (newTransmission || newTask)
		{
			// checks if we can stop now
			dcBroker.checkStopCriteria();
		}
	}
		
	void taskFinished(Cloudlet cloudlet)
	{
		newTask = newTransmission = false;
		
		Task t = getTask(cloudlet);
		Host h = allocation.get(t);
		
		scheduler.taskFinished(t, h);
		
		handleOnlineSchedulling();
	}
	
	void transmissionFinished(Task taskSent, HostImpl dest)
	{
		newTask = newTransmission = false;
		
		// tells real broker that a transmission has finished
		dcBroker.transmissionFinished();
		
		Host sender = allocation.get(taskSent);
		scheduler.transmissionFinished(taskSent, sender, dest);
		
		handleOnlineSchedulling();
	}
	
	public double getClock()
	{
		return CloudSim.clock();
	}
	
	public void validateInput()
	{
		StringBuilder error = new StringBuilder();
		
		// for each task verify if it was allocated
		for (TaskImpl t : tasks)
		{
			if (!allocation.containsKey(t))
				error.append("Tasks #" + t.getCsCloudlet().getCloudletId() + " was not assigned to any host.\n");
		}
		
		// for each task, verify if dependencies where transmitted
		for (TaskImpl t : tasks)
		{			
			HostImpl destination = getAssignedHost(t);
			int tID = t.getCsCloudlet().getCloudletId();
			int destID = destination.getCsHost().getId();
			
			// which has dependencies
			for (TaskImpl dep : t.getInternalDependencies() )
			{				
				// every dependency have to be transmitted
				HostImpl sender = getAssignedHost(dep);
				int senderID = sender.getCsHost().getId();
				int depID = dep.getCsCloudlet().getCloudletId();
				
				// if it's not on the same host
				if (destination == sender)
					continue;
				
				if (!transmission.get(dep).contains(destination))
					error.append("Tasks #" + tID + " (on host #"+ senderID + 
							") depends on task #" + depID + " (on host #" + destID + ") and its result was not transmitted.\n");
			}
		}
		
		// TODO: verify cyclic dependencies
		
		if (error.length() > 0)
		{
			// there are error
			System.err.println(error.toString());
			throw new Error("Scheduler allocation is not valid and will not work. See stderr for more info.");
		}
	}

	@Override
	public void assign(Task t, Host h)
	{
		TaskImpl ti = (TaskImpl)t;
		HostImpl hi = (HostImpl)h;
				
		allocation.put(ti, hi);
		
		dcBroker.bindCloudletToVm(ti.getCsCloudlet().getCloudletId(),
				hi.getCsVm().getId());
		
		newTask = true;
	}

	@Override
	public void transmitResult(Task t, List<Host> destination)
	{
		TaskImpl ti = (TaskImpl)t;
		ArrayList<HostImpl> hl;
		HostImpl responsibleHost;
		
		if (allocation.get(ti) == null)
			throw new Error("Attempt to transmit the result of a not allocated task.");
		
		responsibleHost = getAssignedHost(ti);
		
		hl = transmission.get(ti);
		
		if (hl == null)
		{
			hl = new ArrayList<HostImpl>(destination.size());
			transmission.put(ti, hl);
		}
		
		// for each destination
		for (Host h : destination)
		{
			HostImpl dh = (HostImpl)h;
			
			// if destination has not yet been added
			if (!hl.contains(dh))
			{
				// add it
				hl.add(dh);
				responsibleHost.transmit(ti, dh);
				dcBroker.addPendingTransmission();
			}
		}
		
		newTransmission = destination.size() > 0;
		
		// if there's a new transmission and we are online, we need to
		// tell the host to transmit
		if (newTransmission && simulationOnline)
			responsibleHost.updateTransmissionAndYell();
	}

}
