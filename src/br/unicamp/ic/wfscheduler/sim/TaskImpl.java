package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;

import br.unicamp.ic.wfscheduler.Task;

class TaskImpl implements Task
{
	private static int cloudletID = 0;
	private static UtilizationModel utilizationMode = new UtilizationModelFull();
	
	private Cloudlet cloudlet;
	private ArrayList<TaskImpl> dependencies;
	private BrokerImpl broker;
	
	private double processingStartTime;
	private long initialLength;
	
	public TaskImpl(long length, long outputSize, BrokerImpl broker)
	{
		cloudlet = new Cloudlet(cloudletID++, 
				length, 1, 0, outputSize, utilizationMode, 
				utilizationMode, utilizationMode);
		cloudlet.setUserId(broker.getUserID());
		
		this.broker = broker;
		
		processingStartTime = -1;
		initialLength = length;
		
		dependencies = new ArrayList<TaskImpl>();
	}
	
	Cloudlet getCsCloudlet()
	{
		return cloudlet;
	}
	
	/**
	 * Get processing cost if task was executed on host h
	 */
	double calculateProcessingCost(HostImpl h)
	{
		return cloudlet.getActualCPUTime() * h.getCost();
	}
	
	/**
	 * Add a dependent taks.
	 * @param t dependent task
	 */
	void addDependency(Task t)
	{
		dependencies.add((TaskImpl)t);
	}
	
	/**
	 * This is the same as getDependencies(), just returning the package type
	 * @return see getDependencies()
	 */
	ArrayList<TaskImpl> getInternalDependencies()
	{
		return dependencies;
	}
	
	/**
	 * Returns whether this task has finished or not
	 * @return true if task's finished
	 */
	boolean hasFinished()
	{
		return getCsCloudlet().isFinished();
	}
	
	/**
	 * Get the greatest dependency time
	 * @return time when dependencies will be available (negative if its an expectation - so you should query again when time arrives) 
	 */
	double getDependencyReadyTime()
	{
		double time;
		boolean expectation;
		
		expectation = false;
		time = 0;
		
		// get host I was assigned to
		HostImpl destination = broker.getAssignedHost(this);
		
		for (TaskImpl dependency : dependencies)
		{
			double expectedTime = destination.getResultReadyTime(dependency);
		
			expectation = expectation || expectedTime < 0;
			
			expectedTime = Math.abs(expectedTime);
			
			time = Math.max(time, expectedTime);
		}

		if (expectation)
			time = -time;
		
		return time;
	}
	
	/**
	 * Get time when this task's processing has started
	 * @return
	 */
	double getProcessingStartTime()
	{
		return processingStartTime;
	}
	
	/**
	 * Set this task's processing start time
	 */
	void setProcessingStartTime(double time)
	{
		processingStartTime = time;
	}
	
	/**
	 * Get the time to transmit this task's results to another host (supposing no queue waiting).
	 * @return task's results transmission time
	 */
	double getTransmissionTime()
	{
		return (double)getOutputSize() / (double)broker.getBandwidth();
	}
	
	/**
	 * Get task initial length
	 * @return
	 */
	long getTaskInitialLength()
	{
		return initialLength;
	}
	
	/**
	 * Get the time it's needed to process this task on a host
	 * @param host where this task could be processed 
	 * @return time needed to process task
	 */
	double getProcessingTime(HostImpl host)
	{
		return (double)getCsCloudlet().getCloudletLength() / (double)host.getProcessingSpeed();
	}
	
	/**
	 * Get the time we expect that this task will be finished.
	 * Do not call it if task is finished or you'll get a wrong result.
	 * @return future time when task'll be finished
	 */
	double getCurrentFinishTimeEstimation()
	{		
		return (double)(cloudlet.getCloudletLength() - cloudlet.getCloudletFinishedSoFar()) 
			/ (double)broker.getAssignedHost(this).getProcessingSpeed();
	}
	
	@Override
	public int getID()
	{
		return cloudlet.getCloudletId();		
	}
	
	@Override
	public long getLength()
	{
		return cloudlet.getCloudletLength();
	}

	@Override
	public List<Task> getDependencies()
	{ 
		return new ArrayList<Task>(dependencies);
	}

	@Override
	public long getOutputSize()
	{
		return cloudlet.getCloudletOutputSize();
	}

	@Override
	public boolean hasDependencies()
	{
		return dependencies != null && dependencies.size() > 0;
	}

}
