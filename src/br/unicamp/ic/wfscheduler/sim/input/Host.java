package br.unicamp.ic.wfscheduler.sim.input;

public class Host
{
	private int processorCount;
	private long mips;
	
	public int getProcessorCount()
	{
		return processorCount;
	}

	public long getMips()
	{
		return mips;
	}

	public Host(long mips, int processorCount)
	{
		this.processorCount = processorCount;
		this.mips = mips;
	}
}
