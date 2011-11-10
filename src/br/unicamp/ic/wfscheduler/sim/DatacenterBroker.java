package br.unicamp.ic.wfscheduler.sim;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.core.SimEvent;

public class DatacenterBroker extends org.cloudbus.cloudsim.DatacenterBroker
{
	private BrokerImpl broker;
	
	public DatacenterBroker(BrokerImpl broker) throws Exception 
	{
		super("broker");
		
		this.broker = broker;		
	}

	@Override
	protected void processCloudletReturn(SimEvent ev)
	{
		super.processCloudletReturn(ev);
		
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		
		broker.taskFinished(cloudlet);
	}
	
}
