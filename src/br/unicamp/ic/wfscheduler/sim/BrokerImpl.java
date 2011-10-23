package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
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
	
	private IScheduler scheduler;
	
	public BrokerImpl(List<br.unicamp.ic.wfscheduler.sim.input.Host> hosts,
			List<br.unicamp.ic.wfscheduler.sim.input.Task> tasks,
			long bandwidth, double processingCost,
			IScheduler scheduler) throws Exception
	{
		ArrayList<org.cloudbus.cloudsim.Host> hostList = new ArrayList<org.cloudbus.cloudsim.Host>(hosts.size());
		ArrayList<Vm> vmList = new ArrayList<Vm>(hosts.size());
		ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>(tasks.size());
		
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
		
		dcBroker.submitCloudletList(cloudletList);
		dcBroker.submitVmList(vmList);
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
			totalCost += t.getCsCloudlet().getProcessingCost();
		
		return totalCost;
	}
	
	/**
	 * Get total elapsed time
	 * @return
	 */
	double getTotalTime()
	{
		return CloudSim.clock();
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
		
		dcBroker.bindCloudletToVm(ti.getCsCloudlet().getCloudletId(),
				hi.getCsVm().getId());
	}

}
