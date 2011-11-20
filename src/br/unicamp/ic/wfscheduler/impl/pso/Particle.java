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
	private Position position;
	private Velocity velocity;
	
	public Particle(int dimension)
	{
		this.position = new Position(dimension);
		this.velocity = new Velocity(dimension);
	}	
	
	public Position getPosition()
	{
		return position;
	}
	
	public void setPosition(Position position)
	{
		this.position = position;
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