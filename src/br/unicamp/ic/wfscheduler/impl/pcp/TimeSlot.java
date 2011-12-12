package br.unicamp.ic.wfscheduler.impl.pcp;

public class TimeSlot {
	
	private double startTime;
	private double finishTime;

	public TimeSlot(double startTime, double finishTime) {
		super();
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
	
	public TimeSlot() {
		super();
		this.startTime = 0;
		this.finishTime = -1;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}
	
	
	
	

}
