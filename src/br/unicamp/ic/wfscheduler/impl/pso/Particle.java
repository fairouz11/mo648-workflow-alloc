/**
 * Particle Class
 * 
 * Description:
 * At this model of the PSO heuristic the particle element represents a mapping of resource to a task.
 * If an input workflow has 5 tasks than each generated particle will have 5-Dimension
 * because of 5 tasks and the content of each dimension of the particles will be the
 * compute resource (host number) assigned to that task.
 */
package br.unicamp.ic.wfscheduler.impl.pso;

public class Particle
{
	private Position location;
	private Velocity velocity;
	private double fitness;
	
	public void calculateFitness()
	{
		//Ainda tenho que decifrar o que colocar aqui.
	}
	
	public double getFitness()
	{
		calculateFitness();
		return fitness;
	}
	
	public Position getLocation()
	{
		return location;
	}
	
	public void setLocation(Position location)
	{
		this.location = location;
	}
	
	public Velocity getVelocity()
	{
		return velocity;
	}
	
	public void setVelocity(Velocity velocity)
	{
		this.velocity = velocity;
	}
	
}
