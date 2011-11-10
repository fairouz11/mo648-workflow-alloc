package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.lists.VmList;

public class DatacenterBroker extends org.cloudbus.cloudsim.DatacenterBroker
{
	private BrokerImpl broker;
	private long pendingTransmissions;
	
	public DatacenterBroker(BrokerImpl broker) throws Exception 
	{
		super("broker");
		
		this.pendingTransmissions = 0;
		this.broker = broker;		
	}

	void addPendingTransmission()
	{
		pendingTransmissions++;
	}
	
	void transmissionFinished()	
	{
		pendingTransmissions--;
	}
	
	@Override
	protected void processCloudletReturn(SimEvent ev) 
	{
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		
		getCloudletReceivedList().add(cloudlet);
		
		Log.printLine(CloudSim.clock()+": "+getName()+ ": Cloudlet "+cloudlet.getCloudletId()+" received");
		
		cloudletsSubmitted--;
		
		checkStopCriteria();
	}
	
	void checkStopCriteria()
	{
		// either way, let's guarantee that there's nothing that need to be submitted prior of finishing
		submitCloudlets();
		
		if (getCloudletList().size()==0&&cloudletsSubmitted==0 && pendingTransmissions == 0) //all cloudlets executed and transmissions finished
		{
			Log.printLine(CloudSim.clock()+": "+getName()+ ": All Cloudlets executed. Finishing...");
			clearDatacenters();
			finishExecution();
		}
	}
}
