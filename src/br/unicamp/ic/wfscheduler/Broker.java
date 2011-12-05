package br.unicamp.ic.wfscheduler;

import java.util.List;

/**
 * Responsible for assigning tasks to hosts.
 */
public interface Broker
{
	/**
	 * Datacenter bandwidth in MB/s
	 * @return
	 */
	public long getBandwidth();
	
	/**
	 * Tasks to be executed.
	 * @return
	 */
	public List<Task> getTasks();
	
	/**
	 * Available hosts.
	 * @return
	 */
	public List<Host> getHosts();
	
	/**
	 * Get deadline
	 * @return deadline in seconds
	 */	
	public double getDeadline();
	
	/**
	 * Get current clock time.
	 * @return current clock.
	 */
	public double getClock();
	
	/**
	 * Assign task to be executed on host.
	 * Note that there is no guarantee on dependency order. The scheduler implementation
	 * must enforce any dependency policy.
	 * @param t task to be executed
	 * @param h host where execution should take place 
	 */
	public void assign(Task t, Host h);
	
	/**
	 * Transmit the result of task t to each host in destination list.
	 * The transmission in done in the same order of the list, one at time.
	 * If you call this multiple times, the ordering between calls is kept.
	 * @param t task which result should be transmitted
	 * @param destination list of destinations
	 */
	public void transmitResult(Task t, List<Host> destination);
	
	/**
	 * Validate allocation input.
	 * Verifies if all task were allocated and if transmissions were assigned.
	 * An error is thrown if any issue is encountered.
	 * You may call this method after you've allocated all tasks and transmission.
	 */
	public void validateInput();
}
