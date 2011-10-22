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
	 * Callback when a task is finished
	 * @param t task which has finished
	 * @param h host where task was executed
	 */
	public void taskFinished(Task t, Host h);
}
