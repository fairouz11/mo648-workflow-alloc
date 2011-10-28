/**
 * Particle Swarm Optimization Scheduler Main Class
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.List;

import br.unicamp.ic.wfscheduler.*;


public class PSOScheduler implements IScheduler, Constants
{
	/**
	 * Atributos
	 */
	private List<Task> tasks;
	private List<Host> hosts;
	private long bandwidth;
	
	
	public void startScheduler(Broker broker)
	{
		
	}
	
	public void Scheduling_Heuristic()
	{
		//Step 1: Calculate average computation cost of all tasks in all compute resources
		
		//Step 2: Calculate average cost of communication between resources
		
		//Step 3: Set task node weight w(k,j) as average computation cost
		
		//Step 4: Set edge weight e(k1,k2) as size of file transferred between tasks
		
		//Step 5: Compute PSO({ a set of all tasks })
		PSO_Algorithm(tasks);
		
		//Step 6: Repeat a code block until there are unscheduled tasks
		
	}
	
	/**
	 * The PSO_Algorithm function receives a list with all tasks
	 * @param tasks
	 */
	public void PSO_Algorithm(List<Task> tasks)
	{
		//Step 1: Set particle dimension as equal to the size of ready tasks in "tasks"
		
		//Step 2: Initialize particles position randomly and velocity v randomly
		
		//Step 3: For each particle, calculate its fitness value
		
		//Step 4: If the fitness value is better than the previous best pbest, set the current fitness value as the new pbest
		
		//Step 5: Select the best particle as gbest
		
		//Step 6: For all particles, calculate velocity and update their positions
		
	}
	
	/**
	 * Call back when a task is finished
	 */
	public void taskFinished(Task t, Host h)
	{
		
	}
}
