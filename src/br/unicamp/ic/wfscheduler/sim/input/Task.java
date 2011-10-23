package br.unicamp.ic.wfscheduler.sim.input;

public class Task
{
	private long length;
	private long outputSize;
	
	public long getLength()
	{
		return length;
	}
	
	public long getOutputSize()
	{
		return outputSize;
	}
	
	public Task(long length, long outputSize)
	{
		this.length = length;
		this.outputSize = outputSize;
	}
}
