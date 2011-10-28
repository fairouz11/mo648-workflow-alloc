/**
 * Class Position
 * 
 * Description:
 * Each particle has a position. At any instance of time the position is influenced by its
 * best position and the position of the best particle in the problem space.
 * In our implementation the particle has a position in a 5-Dimensional space. 
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.List;

public class Position
{
	private double x_position;
	
	public Position(double x_position)
	{
		this.x_position = x_position;
	}
	
	public void setPosition(double x_position)
	{
		this.x_position = x_position;
	}
	
	public double getPosition()
	{
		return x_position;
	}
}
