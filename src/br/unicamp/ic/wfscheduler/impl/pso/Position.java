/**
 * Class Position
 * 
 * Description:
 * Each particle has a position. At any instance of time the position is influenced by its
 * best position and the position of the best particle in the problem space.
 * In our implementation the particle has a position in a 5-Dimensional space. 
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.ArrayList;

public class Position
{
	private ArrayList<Double> position;
	
	public Position()
	{
		this.position = new ArrayList<Double>();
	}
	
	public void addPosition(int index, double value)
	{
		this.position.add(index, value);
	}
	
	public void setPosition(int index, double value)
	{
		this.position.set(index, value);
	}
	
	public ArrayList<Double> getPosition()
	{
		return position;
	}
	
	public double getIndexValue(int index)
	{
		return position.get(index);
	}
}
