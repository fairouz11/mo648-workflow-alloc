package br.unicamp.ic.wfscheduler.impl.pcp;

public class TimeSlot {
	
	private long startTime;
	private long finishTime;

	public TimeSlot(long startTime, long finishTime) {
		super();
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
	
	public TimeSlot() {
		super();
		this.startTime = 0;
		this.finishTime = -1;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	
	
	
	

}
