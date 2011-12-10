package br.unicamp.ic.wfscheduler.sim;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

class Datacenter extends org.cloudbus.cloudsim.Datacenter
{
	static final double MinTimeSpan = 0.0000001;
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
	
	@Override
	protected void updateCloudletProcessing() {
		//Log.printLine(CloudSim.clock()+": PowerDatacenter #"+this.get_id()+": updating cloudlet processing.......................................");
		//if some time passed since last processing
		if (CloudSim.clock() > this.getLastProcessTime()) {
			List<? extends Host> list = getVmAllocationPolicy().getHostList();
			double smallerTime = Double.MAX_VALUE;
			//for each host...
			for (int i = 0; i < list.size(); i++) {
				Host host = list.get(i);
				double time = host.updateVmsProcessing(CloudSim.clock());//inform VMs to update processing
				//what time do we expect that the next cloudlet will finish?
				if (time < smallerTime) {
					smallerTime = time;
				}
			}
			//schedules an event to the next time, if valid
			//if (smallerTime > CloudSim.clock() + 0.01 && smallerTime != Double.MAX_VALUE && smallerTime < getSchedulingInterval()) {
			if (smallerTime >= CloudSim.clock() + MinTimeSpan && smallerTime != Double.MAX_VALUE) {
				schedule(getId(), (smallerTime - CloudSim.clock()), CloudSimTags.VM_DATACENTER_EVENT);
			}
			setLastProcessTime(CloudSim.clock());
		}
	}
}
