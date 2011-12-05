package br.unicamp.ic.wfscheduler.sim.input;

public class Host
{
	private int processorCount;
	private long mips;
	private double cost;
	
	public int getProcessorCount()
	{
		return processorCount;
	}
	
	public double getProcessingCost()
	{
		return cost;
	}

	public long getMips()
	{
		return mips;
	}

	public Host(long mips, int processorCount, double cost)
	{
		this.processorCount = processorCount;
		this.mips = mips;
		this.cost = cost;
	}
}
