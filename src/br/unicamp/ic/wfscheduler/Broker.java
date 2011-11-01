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
	 * @param t task which result should be transmitted
	 * @param destination list of destinations
	 * throws Exception if task t was not allocated before the transmit request or if a previous call to transmit the same task was already been made.
	 */
	public void transmitResult(Task t, List<Host> destination) throws Exception;
}
