package br.unicamp.ic.wfscheduler.impl.pcp;
import java.util.ArrayList;
import java.util.Comparator;

import br.unicamp.ic.wfscheduler.*;

public class Assignment {
	
	private Host host;
	private double cost;
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	private double startTime;
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public Assignment(Host host, double startTime, double cost) {
		super();
		this.host = host;
		this.startTime = startTime;
		this.cost = cost;
	}
	
	 
	  
	 
        
          

	
	
	public ArrayList<Assignment> ordenaAssignments(ArrayList<Assignment> assignments){
		return null;
	}
	
	
	

}
