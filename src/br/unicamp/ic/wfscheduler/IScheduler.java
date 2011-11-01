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
}
