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
		
		broker.taskFinished(cloudlet);
		
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
	
	@Override
	protected void submitCloudlets() {
		int vmIndex = 0;
		for (Cloudlet cloudlet : getCloudletList()) 
		{
			Vm vm;
			if (cloudlet.getVmId() != -1) 
			{
				//submit to the specific vm
				vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
				if (vm == null) { // vm was not created
					Log.printLine(CloudSim.clock()+": "+getName()+ ": Postponing execution of cloudlet "+cloudlet.getCloudletId()+": bount VM not available");
					continue;
				}
			
				Log.printLine(CloudSim.clock()+": "+getName()+ ": Sending cloudlet "+cloudlet.getCloudletId()+" to VM #"+vm.getId());
				cloudlet.setVmId(vm.getId());
				sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
				cloudletsSubmitted++;
				vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
				getCloudletSubmittedList().add(cloudlet);
			}
		}
		
		// remove submitted cloudlets from waiting list
		for (Cloudlet cloudlet : getCloudletSubmittedList()) 
		{
			getCloudletList().remove(cloudlet);
		}
	}
	
}
