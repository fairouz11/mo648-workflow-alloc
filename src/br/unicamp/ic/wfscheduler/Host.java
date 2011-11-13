package br.unicamp.ic.wfscheduler;

/**
 * A host which can execute tasks.
 */
public interface Host
{
	/**
	 * Host's unique id.
	 * @return host's id
	 */
	public int getID();
	
	/**
	 * Host's processing speed (in MIPS).
	 * @return
	 */
	public long getProcessingSpeed(); 
	
	/**
	 * Cost per MI (million of instructions)
	 * @return
	 */
	public double getCost();
	
	/**
	 * Number of processors available on this host.
	 * @return
	 */
	public long getProcessorCount();
}
