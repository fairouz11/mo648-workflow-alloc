package br.unicamp.ic.wfscheduler;

import java.util.List;

/**
 * A task to be executed on hosts.
 */
public interface Task
{
	/**
	 * Instruction count (MI - million instructions)
	 * @return
	 */
	public long getLength();
	
	/**
	 * Tasks that should be executed before this.
	 * @return
	 */
	public List<Task> getDependencies();
	
	/**
	 * Get information about dependencies.
	 * @return true if there are dependencies
	 */
	public boolean hasDependencies();
		
	
	/**
	 * Task output size in bytes
	 * @return
	 */
	public long getOutputSize();
}
