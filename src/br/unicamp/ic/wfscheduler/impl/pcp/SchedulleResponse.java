package br.unicamp.ic.wfscheduler.impl.pcp;

import java.util.HashMap;

import br.unicamp.ic.wfscheduler.Task;



public class SchedulleResponse {
	
	private Task failTask;
	private double suggestedStartTime;
	private boolean successful;
	private HashMap<Task, Assignment> schedulle;
	
	
		public HashMap<Task, Assignment> getSchedulle() {
		return schedulle;
	}

	public void setSchedulle(HashMap<Task, Assignment> schedulle) {
		this.schedulle = schedulle;
	}

	public SchedulleResponse() {
		super();
		schedulle = null;
	//	this.failTask = null;
		//this.suggestedStartTime =  -1;
		//this.successful = (Boolean) null;
	}
	
	public Task getFailTask() {
		return failTask;
	}
	public void setFailTask(Task failTask) {
		this.failTask = failTask;
	}
	public double getSuggestedStartTime() {
		return suggestedStartTime;
	}
	public void setSuggestedStartTime(double suggestedStartTime) {
		this.suggestedStartTime = suggestedStartTime;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	
	
	
}
