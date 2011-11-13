package br.unicamp.ic.wfscheduler.impl.pcp;

import br.unicamp.ic.wfscheduler.Task;



public class SchedulleResponse {
	
	private Task failTask;
	private long suggestedStartTime;
	private boolean successful;
	
	
	
	public SchedulleResponse(Task failTask, long suggestedStartTime,
			boolean successful) {
		super();
		this.failTask = failTask;
		this.suggestedStartTime = suggestedStartTime;
		this.successful = successful;
	}
	public SchedulleResponse() {
		super();
		this.failTask = null;
		this.suggestedStartTime = (Long) null;
		this.successful = (Boolean) null;
	}
	
	public Task getFailTask() {
		return failTask;
	}
	public void setFailTask(Task failTask) {
		this.failTask = failTask;
	}
	public long getSuggestedStartTime() {
		return suggestedStartTime;
	}
	public void setSuggestedStartTime(long suggestedStartTime) {
		this.suggestedStartTime = suggestedStartTime;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	
	
	
}
