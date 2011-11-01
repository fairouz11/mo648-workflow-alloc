package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
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
	 * Available task's results.
	 */
	private ArrayList<TaskImpl> taskResults;
	
	public HostImpl(long mips, int processorCount, BrokerImpl broker)
	{		
		if (processorCount < 1)
			throw new RuntimeException("Invalid processorCount");
		
		this.broker = broker;
		this.taskResults = new ArrayList<TaskImpl>();
		
		List<Pe> peList = new ArrayList<Pe>((int)processorCount);
		
		for (int i = 0; i < processorCount; i++)
			peList.add(new Pe(peID++, new PeProvisionerSimple(mips)));
		
		host = new Host(hostID++, new RamProvisionerSimple(ramDefault), 
				new BwProvisionerSimple(broker.getBandwidth()),
				storageDefault, peList, new VmSchedulerTimeShared(peList));
		
		vm = new Vm(vmID++, broker.getUserID(), mips, processorCount, 
				ramDefault, broker.getBandwidth(), storageDefault, "vm",
				new CloudletSchedulerSpaceShared());
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
	
	void addTaskResult(TaskImpl t)
	{
		taskResults.add(t);
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
