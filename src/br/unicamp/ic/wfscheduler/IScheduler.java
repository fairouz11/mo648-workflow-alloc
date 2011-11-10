package br.unicamp.ic.wfscheduler;

/**
 * Interface for scheduler implementations.
 */
public interface IScheduler
{
	/**
	 * Start scheduling. 
	 * @param broker broker which the scheduler should communicate with
	 */
	public void startScheduler(Broker broker);
	
	/**
	 * Callback when a task is finished.
	 * @param task task which has finished
	 * @param host host where task was executed
	 */
	public void taskFinished(Task task, Host host);
	
	/**
	 * Callback when a transmission is finished.
	 * When a task is executed on a host H, may there be a transmission from H to H
	 * with 0 delay.
	 * @param task task which output data was sent
	 * @param sender host which sent data
	 * @param destination host which received data
	 */
	public void transmissionFinished(Task task, Host sender, Host destionation);	
}
