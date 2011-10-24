package br.unicamp.ic.wfscheduler.impl.pcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.unicamp.ic.wfscheduler.Broker;
import br.unicamp.ic.wfscheduler.Host;
import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.Task;

public class PartialCriticalPathsScheduler implements IScheduler
{
	private List<Task> tasks;
	private List<Host> hosts;
	private HashMap<Task, Task> criticalParents;
	private HashMap<Task, Long> constraints;
	private HashMap<Task, Host> assignment;
	
	
	
	@Override
	public void startScheduler(Broker broker) {
		tasks = broker.getTasks();
		hosts = broker.getHosts();
	}
	
	@Override
	public void taskFinished(Task t, Host h) {
		// TODO Auto-generated method stub
		
	}
	
	private long estimateMinimumTransferTime(List<Host> hosts){
		//We are not considering bandwidth or latency info
		//If we did, this method would return the minimum latency between two hosts
		return 0;
	}
	
	private long estimateMinimumExecutionTime(Task task, List<Host> hosts){
		long met = 999999999;
		for (Iterator<Host> iterator = hosts.iterator(); iterator.hasNext();) {
			Host host = (Host) iterator.next();
			long et = task.getLength()/host.getProcessingSpeed();
			if(met>et)
				met = et;
		}
		return met;
	}
	
	private long estimateEarliestExecutionTime(Task task, List<Host> hosts){
		long maxEST = 0;
		//long min
		for (Iterator<Task> iterator = task.getDependencies().iterator(); iterator.hasNext();) {
			Task parent = (Task) iterator.next();
			long parentEST = estimateEarliestExecutionTime(parent, hosts);
			long parentMET = estimateMinimumExecutionTime(parent, hosts);
			long parentMTT = estimateMinimumTransferTime(hosts);
			long est = parentEST + parentMET + parentMTT;
			if(maxEST<est){
			criticalParents.put(task, parent);	
				maxEST=est;
			}
		}
		return maxEST;
	}
	
	private void addingTasksEntryAndExit(List<Task> tasks){
		Task entry = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		Task exit = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		
		ArrayList<Task> semDependencias = new ArrayList<Task>();
		ArrayList<Task> naoEhDependencia = new ArrayList<Task>();
		
		naoEhDependencia.addAll(tasks);
		
		for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			List<Task>dependencias = task.getDependencies();
			if( dependencias.isEmpty())
				semDependencias.add(task);
			else{
				for (Iterator iterator2 = dependencias.iterator(); iterator2
						.hasNext();) {
					Task dependencia = (Task) iterator2.next();
					if(naoEhDependencia.contains(dependencia)){
						naoEhDependencia.remove(dependencia);
					}
				}
			}
			
		}
		
		for (Iterator iterator = semDependencias.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			task.getDependencies().add(entry);
		}
		
		for (Iterator iterator = naoEhDependencia.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			exit.getDependencies().add(task);
		}
	}
	
	private void scheduleWorkflow(List<Task> task, List<Host> hosts){
		
	}
	
	
}
