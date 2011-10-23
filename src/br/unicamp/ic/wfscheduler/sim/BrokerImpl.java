package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
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
	
	private long bandwidth;
	private double processingCost;
	private Datacenter datacenter;
	private DatacenterCharacteristics caracteristics;
	private DatacenterBroker dcBroker;
	
	private ArrayList<HostImpl> hosts;
	private ArrayList<TaskImpl> tasks;
	private Hashtable<TaskImpl, HostImpl> allocation;
	
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
		
		this.allocation = new Hashtable<TaskImpl, HostImpl>(tasks.size());
		this.hosts = new ArrayList<HostImpl>(hosts.size());
		this.scheduler = scheduler;
		this.tasks = new ArrayList<TaskImpl>(tasks.size());
		this.bandwidth = bandwidth;
		this.processingCost = processingCost;
		this.dcBroker = new DatacenterBroker();
		this.caracteristics = new DatacenterCharacteristics("X86", "linux", "Xen", 
				hostList, 10, processingCost, costPerMem, costPerStorage, costPerBw);
		
		
		for (br.unicamp.ic.wfscheduler.sim.input.Task t : tasks)
		{		
			TaskImpl ti = new TaskImpl(t.getLength(), t.getOutputSize(), this);
			cloudletList.add(ti.getCsCloudlet());
			this.tasks.add(ti);
			
			taskMapping.put(t, ti);
		}
		
		// task dependencies
		for (br.unicamp.ic.wfscheduler.sim.input.Task t : tasks)
		{
			TaskImpl ti = taskMapping.get(t);
			
			for (br.unicamp.ic.wfscheduler.sim.input.Task td : t.getDependencies())
			{
				ti.addDependencie(taskMapping.get(td));
			}
		}
		
		for (br.unicamp.ic.wfscheduler.sim.input.Host h : hosts)
		{
			HostImpl hi = new HostImpl(h.getMips(), h.getProcessorCount(), this);
			vmList.add(hi.getCsVm());
			this.hosts.add(hi);
			hostList.add(hi.getCsHost());
		}
		
		datacenter = new Datacenter("datacenter", caracteristics, new VmAllocationPolicySimple(hostList),
				new LinkedList<Storage>(), 0);
		
		dcBroker.submitVmList(vmList);
		dcBroker.submitCloudletList(cloudletList);		
	}
	
	void start()
	{
		scheduler.startScheduler(this);
		
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

	@Override
	public List<Host> getHosts()
	{
		return new ArrayList<Host>(hosts);
	}

	@Override
	public void assign(Task t, Host h)
	{
		TaskImpl ti = (TaskImpl)t;
		HostImpl hi = (HostImpl)h;
		
		allocation.put(ti, hi);
		
		dcBroker.bindCloudletToVm(ti.getCsCloudlet().getCloudletId(),
				hi.getCsVm().getId());
	}

}
