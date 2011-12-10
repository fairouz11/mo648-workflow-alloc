package br.unicamp.ic.wfscheduler.impl.pcp;

import br.unicamp.ic.wfscheduler.Task;

public class TaskAssigned {
	
	private Task task;
	private Assignment assignment;
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public TaskAssigned(Task task, Assignment assignment) {
		super();
		this.task = task;
		this.assignment = assignment;
	}
	
	
	
	
	
}
