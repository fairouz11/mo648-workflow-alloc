/**
 * Velocity class
 * 
 * Description:
 * In the PSO heuristic each particle has a velocity that is changed after each
 * iteration of the algorithm. The movement of each particle is coordinated by a velocity.
 */
package br.unicamp.ic.wfscheduler.impl.pso;


import java.util.List;


public class Velocity implements Constants
{
	public List<Double> velocity;
	
	/**
	 * Construtor da classe
	 */
	public Velocity()
	{
		//ver como faz isso
	}
	
	public List<Double> getVelocity()
	{
		return velocity;
	}
	
	public void setVelocity(List<Double> velocity)
	{
		//ver como faz isso
	}

}
