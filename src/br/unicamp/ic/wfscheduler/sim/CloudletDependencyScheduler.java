package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Pe;
import br.unicamp.ic.wfscheduler.sim.ResCloudlet;
import org.cloudbus.cloudsim.core.CloudSim;

public class CloudletDependencyScheduler extends CloudletScheduler
{
	/** The cloudlet waiting list. */
	private List<? extends ResCloudlet> cloudletWaitingList;

	/** The cloudlet exec list. */
	private List<? extends ResCloudlet> cloudletExecList;

	/** The cloudlet paused list. */
	private List<? extends ResCloudlet> cloudletPausedList;

	/** The cloudlet finished list. */
	private List<? extends ResCloudlet> cloudletFinishedList;

	/** The current CPUs. */
	protected int currentCpus;

	/** The used PEs. */
	protected int usedPes;

	private BrokerImpl broker;
	private HostImpl host;
	
	public CloudletDependencyScheduler(HostImpl host, BrokerImpl broker)
	{	
		super();
		this.cloudletWaitingList = new ArrayList<ResCloudlet>();
		this.cloudletExecList = new ArrayList<ResCloudlet>();
		this.cloudletPausedList = new ArrayList<ResCloudlet>();
		this.cloudletFinishedList = new ArrayList<ResCloudlet>();
		this.usedPes = 0;
		this.currentCpus = 0;
		this.host = host;
		this.broker = broker;		
	}
	
	@Override
	public void cloudletFinish(org.cloudbus.cloudsim.ResCloudlet rcl) 
	{
		rcl.setCloudletStatus(Cloudlet.SUCCESS);
		rcl.finalizeCloudlet();
		getCloudletFinishedList().add((ResCloudlet)rcl);
		usedPes -= rcl.getPesNumber();
		
		// inform host that task has finished
		TaskImpl task = broker.getTask(rcl.getCloudlet());
		host.addTaskResult(task, rcl.getClouddletFinishTime());
	}
	
	double getCapacity()
	{
		// calculate the expected time for cloudlet completion
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : getCurrentMipsShare()) {
			capacity += mips;
			if (mips > 0) {
				cpus++;
			}
		}

		currentCpus = cpus;
		capacity /= cpus;
		
		return capacity;
	}
	
	double growCloudletForDependency(TaskImpl task, Cloudlet cl)
	{
		double nextEvent = Double.MAX_VALUE;
		double currentTime = CloudSim.clock();
		// verify for dependencies		
		double expectedTransmissionFinishedTime = Math.abs(task.getDependencyReadyTime());
		
		// if transmission should finished by now, let's wait a little bit more
		if (expectedTransmissionFinishedTime == CloudSim.clock())
		{
			expectedTransmissionFinishedTime += Datacenter.MinTimeSpan;
		}
		
		// if is there dependencies
		if (expectedTransmissionFinishedTime > currentTime)
		{			
			nextEvent = expectedTransmissionFinishedTime;
		
			// grow task so it'll consider transmission time
			cl.setCloudletLength(task.getTaskInitialLength() + 
					(long)((expectedTransmissionFinishedTime - task.getProcessingStartTime()) * getCapacity()));
		}
		
		return nextEvent;
	}
	
	/**
	 * Updates the processing of cloudlets running under management of this scheduler.
	 *
	 * @param currentTime current simulation time
	 * @param mipsShare array with MIPS share of each processor available to the scheduler
	 *
	 * @return time predicted completion time of the earliest finishing cloudlet, or 0
	 * if there is no next events
	 *
	 * @pre currentTime >= 0
	 * @post $none
	 */
	@Override
	public double updateVmProcessing(double currentTime, List<Double> mipsShare) 
	{		
		setCurrentMipsShare(mipsShare);
		double nextEvent = Double.MAX_VALUE;
		double timeSpam;
		double capacity = getCapacity();
		int finished = 0;
		int cont = 0;
		
		List<ResCloudlet> toRemove = new ArrayList<ResCloudlet>();
		//update each cloudlet
		for (ResCloudlet rcl : getCloudletExecList())
		{ // each machine in the exec list has the same amount of cpu
			
			TaskImpl task = broker.getTask(rcl.getCloudlet());
		
			timeSpam = currentTime - task.getProcessingStartTime(); 
			
			rcl.updateCloudletFinishedSoFar((long) (capacity * timeSpam * rcl.getPesNumber()));
			
			nextEvent = Math.min(growCloudletForDependency(task, rcl.getCloudlet()), nextEvent);			
		
			if (rcl.getRemainingCloudletLength() == 0.0) 
			{
				// finished anyway, rounding issue...
				toRemove.add(rcl);
				cloudletFinish(rcl);
				finished++;
			}		
						
			cont++;			
		}
		
		getCloudletExecList().removeAll(toRemove);

		// for each finished cloudlet, if it's on transmission list, update transmissions
		for (ResCloudlet rcl : toRemove)
		{			
			if (host.isTaskOnTransmissionQueue(broker.getTask(rcl.getCloudlet())))
			{
				host.updateTransmissionAndYell();
				break;
			}
		}

        //for each finished cloudlet, add a new one from the waiting list
		if (!getCloudletWaitingList().isEmpty()) 
		{
			
			for (int i = 0; i < finished; i++) 
			{				
				toRemove.clear();
								
				for (ResCloudlet rcl : getCloudletWaitingList()) 
				{
					
					if ((currentCpus - usedPes) >= rcl.getPesNumber()) 
					{
						rcl.setCloudletStatus(Cloudlet.INEXEC);
						broker.getTask(rcl.getCloudlet()).setProcessingStartTime(CloudSim.clock());
						
						for (int k = 0; k < rcl.getPesNumber(); k++) {
							rcl.setMachineAndPeId(0, i);
						}
						getCloudletExecList().add(rcl);
						usedPes += rcl.getPesNumber();
						toRemove.add(rcl);
						break;
					}
				}
				getCloudletWaitingList().removeAll(toRemove);
				
			}// for(cont)
        }
		
		if (getCloudletExecList().size() == 0 && getCloudletWaitingList().size() == 0) 
		{ 
			// no more cloudlets in this scheduler
			nextEvent = 0;
		}
		else
		{
	        //estimate finish time of cloudlets in the execution queue		
			for (ResCloudlet rcl : getCloudletExecList()) 
			{
				double remainingLength = rcl.getRemainingCloudletLength();
				double estimatedFinishTime = currentTime + (remainingLength / (capacity * rcl.getPesNumber()));
				
//				if (estimatedFinishTime - currentTime < 0.1) {
//					estimatedFinishTime = currentTime + 0.1;
//				}
				
				if (estimatedFinishTime < nextEvent) {
					nextEvent = estimatedFinishTime;
				}
			}
		}
		
		setPreviousTime(currentTime);
		return nextEvent;
	}
	
	/**
	 * Receives an cloudlet to be executed in the VM managed by this scheduler.
	 *
	 * @param cloudlet the submited cloudlet
	 * @param fileTransferTime time required to move the required files from the SAN to the VM
	 *
	 * @return expected finish time of this cloudlet, or 0 if it is in the waiting queue
	 *
	 * @pre gl != null
	 * @post $none
	 */
	@Override
	public double cloudletSubmit(Cloudlet cloudlet, double fileTransferTime) 
	{	
		double nextEvent;
		
		if ((currentCpus - usedPes) >= cloudlet.getPesNumber()) 
		{
			// it can go to the exec list
			ResCloudlet rcl = new ResCloudlet(cloudlet);
			rcl.setCloudletStatus(Cloudlet.INEXEC);
			broker.getTask(cloudlet).setProcessingStartTime(CloudSim.clock());
			
			for (int i = 0; i < cloudlet.getPesNumber(); i++) {
				rcl.setMachineAndPeId(0, i);
			}
						
			double ne = growCloudletForDependency(broker.getTask(rcl.getCloudlet()), rcl.getCloudlet());
			
			nextEvent = Math.min(ne,
					cloudlet.getCloudletLength() / getCapacity());
			
			getCloudletExecList().add(rcl);
			usedPes += cloudlet.getPesNumber();
		} 
		else 
		{
			// no enough free PEs: go to the waiting queue		
			ResCloudlet rcl = new ResCloudlet(cloudlet);
			rcl.setCloudletStatus(Cloudlet.QUEUED);
			getCloudletWaitingList().add(rcl);

			nextEvent = 0;
		}
		
		return nextEvent;
	}

	/* (non-Javadoc)
	 * @see cloudsim.CloudletScheduler#cloudletSubmit(cloudsim.Cloudlet)
	 */
	@Override
	public double cloudletSubmit(Cloudlet cloudlet) {
		cloudletSubmit(cloudlet, 0);
		return 0;
	}

	@Override
	public Cloudlet cloudletCancel(int cloudletId) 
	{
		throw new Error("DO NOT USE CANCEL");
	}

	@Override
	public boolean cloudletPause(int cloudletId) 
	{
		throw new Error("DO NOT USE PAUSE");
	}

	@Override
	public double cloudletResume(int cloudletId) 
	{
		throw new Error("DO NOT USE RESUME");
	}
	
	/**
	 * Gets the status of a cloudlet.
	 *
	 * @param cloudletId ID of the cloudlet
	 *
	 * @return status of the cloudlet, -1 if cloudlet not found
	 *
	 * @pre $none
	 * @post $none
	 */
	@Override
	public int getCloudletStatus(int cloudletId) {
		for (ResCloudlet rcl : getCloudletExecList()) {
			if (rcl.getCloudletId() == cloudletId) {
				return rcl.getCloudletStatus();
			}
		}

		for (ResCloudlet rcl : getCloudletPausedList()) {
			if (rcl.getCloudletId() == cloudletId) {
				return rcl.getCloudletStatus();
			}
		}

		for (ResCloudlet rcl : getCloudletWaitingList()) {
			if (rcl.getCloudletId() == cloudletId) {
				return rcl.getCloudletStatus();
			}
		}

		return -1;
	}

	/**
	 * Get utilization created by all cloudlets.
	 *
	 * @param time the time
	 *
	 * @return total utilization
	 */
	@Override
	public double getTotalUtilizationOfCpu(double time) {
		double totalUtilization = 0;
		for (ResCloudlet gl : getCloudletExecList()) {
			totalUtilization += gl.getCloudlet().getUtilizationOfCpu(time);
		}
		return totalUtilization;
	}

	/**
	 * Informs about completion of some cloudlet in the VM managed
	 * by this scheduler.
	 *
	 * @return $true if there is at least one finished cloudlet; $false otherwise
	 *
	 * @pre $none
	 * @post $none
	 */
	@Override
	public boolean isFinishedCloudlets() {
		return getCloudletFinishedList().size() > 0;
	}

	/**
	 * Returns the next cloudlet in the finished list, $null if this list is empty.
	 *
	 * @return a finished cloudlet
	 *
	 * @pre $none
	 * @post $none
	 */
	@Override
	public Cloudlet getNextFinishedCloudlet() {
		if (getCloudletFinishedList().size() > 0) {
			return getCloudletFinishedList().remove(0).getCloudlet();
		}
		return null;
	}

	/**
	 * Returns the number of cloudlets runnning in the virtual machine.
	 *
	 * @return number of cloudlets runnning
	 *
	 * @pre $none
	 * @post $none
	 */
	@Override
	public int runningCloudlets() {
		return getCloudletExecList().size();
	}

	/**
	 * Returns one cloudlet to migrate to another vm.
	 *
	 * @return one running cloudlet
	 *
	 * @pre $none
	 * @post $none
	 */
	@Override
	public Cloudlet migrateCloudlet() 
	{
		throw new Error("DO NOT USE MIGRATE");
	}

	/**
	 * Gets the cloudlet waiting list.
	 *
	 * @param <T> the generic type
	 * @return the cloudlet waiting list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResCloudlet> List<T> getCloudletWaitingList() {
		return (List<T>) cloudletWaitingList;
	}

	/**
	 * Cloudlet waiting list.
	 *
	 * @param <T> the generic type
	 * @param cloudletWaitingList the cloudlet waiting list
	 */
	protected <T extends ResCloudlet> void cloudletWaitingList(List<T> cloudletWaitingList) {
		this.cloudletWaitingList = cloudletWaitingList;
	}

	/**
	 * Gets the cloudlet exec list.
	 *
	 * @param <T> the generic type
	 * @return the cloudlet exec list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResCloudlet> List<T> getCloudletExecList() {
		return (List<T>) cloudletExecList;
	}

	/**
	 * Sets the cloudlet exec list.
	 *
	 * @param <T> the generic type
	 * @param cloudletExecList the new cloudlet exec list
	 */
	protected <T extends ResCloudlet> void setCloudletExecList(List<T> cloudletExecList) {
		this.cloudletExecList = cloudletExecList;
	}

	/**
	 * Gets the cloudlet paused list.
	 *
	 * @param <T> the generic type
	 * @return the cloudlet paused list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResCloudlet> List<T> getCloudletPausedList() {
		return (List<T>) cloudletPausedList;
	}

	/**
	 * Sets the cloudlet paused list.
	 *
	 * @param <T> the generic type
	 * @param cloudletPausedList the new cloudlet paused list
	 */
	protected <T extends ResCloudlet> void setCloudletPausedList(List<T> cloudletPausedList) {
		this.cloudletPausedList = cloudletPausedList;
	}

	/**
	 * Gets the cloudlet finished list.
	 *
	 * @param <T> the generic type
	 * @return the cloudlet finished list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResCloudlet> List<T> getCloudletFinishedList() {
		return (List<T>) cloudletFinishedList;
	}

	/**
	 * Sets the cloudlet finished list.
	 *
	 * @param <T> the generic type
	 * @param cloudletFinishedList the new cloudlet finished list
	 */
	protected <T extends ResCloudlet> void setCloudletFinishedList(List<T> cloudletFinishedList) {
		this.cloudletFinishedList = cloudletFinishedList;
	}

	/* (non-Javadoc)
	 * @see org.cloudbus.cloudsim.CloudletScheduler#getCurrentRequestedMips()
	 */
	@Override
	public List<Double> getCurrentRequestedMips() {
		List<Double> mipsShare = new ArrayList<Double>();
		if (getCurrentMipsShare() != null) {
			for (Double mips : getCurrentMipsShare()) {
				mipsShare.add(mips);
			}
		}
		return mipsShare;
	}

	/* (non-Javadoc)
	 * @see org.cloudbus.cloudsim.CloudletScheduler#getTotalCurrentAvailableMipsForCloudlet(org.cloudbus.cloudsim.ResCloudlet, java.util.List)
	 */
	@Override
	public double getTotalCurrentAvailableMipsForCloudlet(org.cloudbus.cloudsim.ResCloudlet rcl, List<Double> mipsShare) {
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : mipsShare) { // count the cpus available to the vmm
			capacity += mips;
			if (mips > 0) {
				cpus++;
			}
		}
		currentCpus = cpus;
		capacity /= cpus; // average capacity of each cpu
		return capacity;
	}

	/* (non-Javadoc)
	 * @see org.cloudbus.cloudsim.CloudletScheduler#getTotalCurrentAllocatedMipsForCloudlet(org.cloudbus.cloudsim.ResCloudlet, double)
	 */
	@Override
	public double getTotalCurrentAllocatedMipsForCloudlet(org.cloudbus.cloudsim.ResCloudlet rcl, double time) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see org.cloudbus.cloudsim.CloudletScheduler#getTotalCurrentRequestedMipsForCloudlet(org.cloudbus.cloudsim.ResCloudlet, double)
	 */
	@Override
	public double getTotalCurrentRequestedMipsForCloudlet(org.cloudbus.cloudsim.ResCloudlet rcl, double time) {
		// TODO Auto-generated method stub
		return 0.0;
	}
}
