package br.unicamp.ic.wfscheduler.impl.pcp;
import br.unicamp.ic.wfscheduler.*;

public class Assignment {
	
	private Host host;
	private long cost;
	public long getCost() {
		return cost;
	}
	public void setCost(long cost) {
		this.cost = cost;
	}
	private long startTime;
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public Assignment(Host host, long startTime, long cost) {
		super();
		this.host = host;
		this.startTime = startTime;
		this.cost = cost;
	}
	
	
	
	

}
