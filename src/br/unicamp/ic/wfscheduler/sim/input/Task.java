package br.unicamp.ic.wfscheduler.sim.input;

import java.util.ArrayList;
import java.util.List;

public class Task
{
	private long length;
	private long outputSize;
	private ArrayList<Task> dependencies;
	
	public long getLength()
	{
		return length;
	}
	
	public long getOutputSize()
	{
		return outputSize;
	}
	
	public List<Task> getDependencies()
	{
		return dependencies;
	}
	
	public void addDependencies(Task t)
	{
		dependencies.add(t);
	}
	
	public Task(long length, long outputSize)
	{
		this.length = length;
		this.outputSize = outputSize;
		this.dependencies = new ArrayList<Task>();
	}
}
