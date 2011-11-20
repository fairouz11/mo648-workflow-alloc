/**
 * Class Position
 * 
 * Description:
 * Each particle has a position. At any instance of time the position is influenced by its
 * best position and the position of the best particle in the problem space.
 * Each dimension of the particle has its own position value.
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.Hashtable;

import br.unicamp.ic.wfscheduler.*;

public class Position
{
	private Hashtable<Task, Integer> position;
	
	public Position()
	{
		this.position = new Hashtable<Task, Integer>();
	}
	
	public Position(int dimension)
	{
		this.position = new Hashtable<Task, Integer>(dimension);
	}
	
	public void addPosition(Task t, int value)
	{
		this.position.put(t, value);
	}
	
	public void setPosition(Task t, int value)
	{
		addPosition(t, value);
	}
	
	public Hashtable<Task, Integer> getPosition()
	{
		return position;
	}
	
	public int getIndexValue(Task t)
	{
		return position.get(t);
	}
}