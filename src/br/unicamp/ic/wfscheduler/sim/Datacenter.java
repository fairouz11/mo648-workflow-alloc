package br.unicamp.ic.wfscheduler.sim;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

class Datacenter extends org.cloudbus.cloudsim.Datacenter
{
	static final int TRANSMISSION_EVENT = 5000;
	
	private BrokerImpl broker;
	
	public Datacenter(String name, DatacenterCharacteristics characteristics, 
			VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval,
			BrokerImpl broker)
			throws Exception
	{
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
		
		this.broker = broker;
	}

	protected void updateTransmissions()
	{
		double nextEvent;
		double ne;
		
		nextEvent = Double.MAX_VALUE;
		
		for (HostImpl h : broker.getInternalHosts())
		{
			ne = h.updateTransmission();
			
			if (ne > 0)
				nextEvent = Math.min(nextEvent, ne);
		}
		
		if (nextEvent > 0 && nextEvent != Double.MAX_VALUE)
			schedule(getId(), nextEvent - CloudSim.clock(), TRANSMISSION_EVENT);
	}	
	
	@Override
	protected void processVmCreate(SimEvent ev, boolean ack) 
	{
		super.processVmCreate(ev, ack);
		
		// begin transmissions (if any)
    	Vm vm = (Vm) ev.getData();
    	HostImpl host = broker.getHost(vm.getHost());
    	host.updateTransmissionAndYell();
	}
	
	@Override
	protected void processOtherEvent(SimEvent ev)
	{
		switch (ev.getTag())
		{
			// TODO find a way to update just a host
			case TRANSMISSION_EVENT:
				updateTransmissions();
				break;
			
			default:
				break;
		}
	}
}
