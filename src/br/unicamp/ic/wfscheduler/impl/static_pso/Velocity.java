/**
 * Velocity class
 * 
 * Description:
 * In the PSO heuristic each particle has a velocity that is changed after each
 * iteration of the algorithm. The movement of a particle is coordinated by its velocity.
 * Each dimension of a particle has its own velocity value.
 */
package br.unicamp.ic.wfscheduler.impl.static_pso;

import java.util.Hashtable;

import br.unicamp.ic.wfscheduler.Task;


public class Velocity
{
	private Hashtable<Task, Double> velocity;
	
	/**
	 * Constructor
	 */
	public Velocity()
	{
		this.velocity = new Hashtable<Task, Double>();
	}
	
	public Velocity(int dimension)
	{
		this.velocity = new Hashtable<Task, Double>(dimension);
	}
	
	public Hashtable<Task, Double> getVelocity()
	{
		return velocity;
	}
	
	public void addVelocity(Task t, double value)
	{
		this.velocity.put(t, value);
	}
	
	public void setVelocity(Task t, double value)
	{
		addVelocity(t, value);
	}
	
	public double getIndexValue(Task t)
	{
		return velocity.get(t);
	}
}