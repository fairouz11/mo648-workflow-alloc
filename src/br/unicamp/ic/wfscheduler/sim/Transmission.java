package br.unicamp.ic.wfscheduler.sim;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

class Transmission
{
	private HostImpl host;	
	private TaskImpl task;
	private double finishTime;
	
	public Transmission(HostImpl host, TaskImpl task)
	{
		this.host = host;
		this.task = task;
		this.finishTime = -1;
	}
	
	public HostImpl getHost()
	{
		return host;
	}

	public TaskImpl getTask()
	{
		return task;
	}
	
	/**
	 * Begin transmission
	 */
	public void beginTransmission()
	{
		finishTime = estinateFinishTime();
	}
	
	/**
	 * Estimates the time when transmission will be finished
	 */
	public double estinateFinishTime()
	{
		return CloudSim.clock() + (double)task.getOutputSize() / (double)host.getBroker().getBandwidth();
	}
	
	/**
	 * Finish transmission time
	 * @return -1 if transmission has not started yet or finish time (may be in the future)
	 */
	public double getFinishTime()
	{
		return finishTime;
	}

	/**
	 * Finishes a transmission
	 */
	public void finishTransmission()
	{	
		// inform host
		host.addTaskResult(task, getFinishTime());
	}
}
