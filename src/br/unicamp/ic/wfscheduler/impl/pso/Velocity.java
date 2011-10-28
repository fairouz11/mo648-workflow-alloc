/**
 * Velocity class
 * 
 * Description:
 * In the PSO heuristic each particle has a velocity that is changed after each
 * iteration of the algorithm. The movement of each particle is coordinated by a velocity.
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.ArrayList;


public class Velocity implements Constants
{
	public ArrayList<Double> velocity;
	
	/**
	 * Construtor da classe
	 */
	public Velocity()
	{
		this.velocity = new ArrayList<Double>();
	}
	
	public ArrayList<Double> getVelocity()
	{
		return velocity;
	}
	
	public void addVelocity(int index, double value)
	{
		this.velocity.add(index, value);
	}
	
	public void setVelocity(int index, double value)
	{
		this.velocity.set(index, value);
	}
	
	public double getIndexValue(int index)
	{
		return velocity.get(index);
	}
}
