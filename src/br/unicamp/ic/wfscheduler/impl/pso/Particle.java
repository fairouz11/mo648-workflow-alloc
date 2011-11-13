/**
 * Particle Class
 * 
 * Description:
 * At this model of the PSO heuristic the particle represents a mapping of resource to a task.
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
	
	public Particle(int dimension){
		this.fitness = 0;
		this.location = new Position(dimension);
		this.velocity = new Velocity(dimension);
	}	
	
	public double getFitness()
	{
		return fitness;
	}
	
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
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
