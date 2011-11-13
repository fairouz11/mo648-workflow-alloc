package br.unicamp.ic.wfscheduler.impl.pcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.unicamp.ic.wfscheduler.*;

public class PartialCriticalPathsScheduler implements IScheduler {
	private List<Task> tasks;
	private List<Host> hosts;
	private long bandwidth;

	private HashMap<Task, Assignment> assignments;
	private HashMap<Task, Long> schedulings;
	private HashMap<Task, Long> constraints;
	private HashMap<Task, Long> METs;
	private HashMap<Task, Long> MTTs;
	private HashMap<Task, Long> ESTs;

	@Override
	public void startScheduler(Broker broker) {
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		bandwidth = broker.getBandwidth();
		long deadline = 999999998;
		scheduleWorkflow(tasks, hosts, deadline);
	}

	private long estimateMinimumTransferTime(Task task, List<Host> hosts) {
		// We are considering uniform bandwidth
		long mtt;
		if (MTTs.containsKey(task)) {
			mtt = MTTs.get(task);
		} else {
			mtt = task.getLength() / bandwidth;
			MTTs.put(task, mtt);
		}
		return mtt;
	}

	private long estimateMinimumExecutionTime(Task task, List<Host> hosts) {
		long met = 999999999;
		if (METs.containsKey(task)) {
			met = METs.get(task);
		} else {
			for (Iterator<Host> iterator = hosts.iterator(); iterator.hasNext();) {
				Host host = (Host) iterator.next();
				long et = task.getLength() / host.getProcessingSpeed();
				if (met > et)
					met = et;
			}
		}
		METs.put(task, met);
		return met;
	}

	private long estimateEarliestStartTime(Task task, List<Host> hosts) {
		long maxEST = 0;
		if (ESTs.containsKey(task)) {
			maxEST = ESTs.get(task);
		} else {
			Task criticalParent = findCriticalParent(task, hosts);
			//maxEST = estimateEarliestStartTime(criticalParent, hosts);
			maxEST = estimateEarliestFinishTime(criticalParent, hosts);
		}
		ESTs.put(task, maxEST);
		return maxEST;
	}

	private Task findCriticalParent(Task task, List<Host> hosts) {
		long maxEST = 0;
		Task criticalParent = null;
		for (Iterator<Task> iterator = task.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (!schedulings.containsKey(parent)) {
				long est = estimateEarliestFinishTime(parent, hosts);
				if (maxEST < est) {
					criticalParent = parent;
					maxEST = est;
				}
			}
		}
		return criticalParent;
	}

	private long estimateEarliestFinishTime(Task task,
			List<Host> hosts) {
		long parentEST = estimateEarliestStartTime(task, hosts);
		long parentMET = estimateMinimumExecutionTime(task, hosts);
		long parentMTT = estimateMinimumTransferTime(task, hosts);
		long eft = parentEST + parentMET + parentMTT;
		return eft;
	}
	
	private List<Task> addingTasksEntryAndExit(List<Task> tasks, Task entry,
			Task exit) {

		ArrayList<Task> semDependencias = new ArrayList<Task>();
		ArrayList<Task> naoEhDependencia = new ArrayList<Task>();

		naoEhDependencia.addAll(tasks);

		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			List<Task> dependencias = task.getDependencies();
			if (dependencias.isEmpty())
				semDependencias.add(task);
			else {
				for (Iterator<Task> iterator2 = dependencias.iterator(); iterator2
						.hasNext();) {
					Task dependencia = (Task) iterator2.next();
					if (naoEhDependencia.contains(dependencia)) {
						naoEhDependencia.remove(dependencia);
					}
				}
			}

		}

		for (Iterator<Task> iterator = semDependencias.iterator(); iterator
				.hasNext();) {
			Task task = (Task) iterator.next();
			task.getDependencies().add(entry);
		}

		for (Iterator<Task> iterator = naoEhDependencia.iterator(); iterator
				.hasNext();) {
			Task task = (Task) iterator.next();
			exit.getDependencies().add(task);
		}
		return tasks;
	}

	private void scheduleWorkflow(List<Task> task, List<Host> hosts,
			long deadline) {

		schedulings = new HashMap<Task, Long>();
		METs = new HashMap<Task, Long>();
		MTTs = new HashMap<Task, Long>();
		ESTs = new HashMap<Task, Long>();

		Task entry = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		Task exit = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		tasks = addingTasksEntryAndExit(tasks, entry, exit);
		schedulings.put(entry, (long) 0);
		schedulings.put(exit, (long) deadline);
		constraints = new HashMap<Task, Long>();
	//	estimateEarliestStartTime(exit, hosts);// apenas para calcular o partial critical path atual
		if (ScheduleParents(exit).isSuccessful()) {
			StartAssignmens();
		}
	}

	private SchedulleResponse ScheduleParents(Task task) {
		if (!hasUnscheduledParents(task)) {
			SchedulleResponse sr = new SchedulleResponse();
			sr.setSuccessful(true);
			return sr;
		}
		Task taskI = task;
		ArrayList<Task> criticalPath = new ArrayList<Task>();
		while (hasUnscheduledParents(taskI)) {
			Task criticalParent = findCriticalParent(taskI, hosts);
			criticalPath.add(0, criticalParent);
			taskI = criticalParent;
		}
		
		while (!isScheduled(criticalPath)) {
			SchedulleResponse criticalPathSchedule = SchedulePath(criticalPath);
			if(!criticalPathSchedule.isSuccessful()){
				return criticalPathSchedule;
			}
			for (Iterator<Task> iterator = criticalPath.iterator(); iterator.hasNext();) {
				Task task2 = (Task) iterator.next();
				SchedulleResponse parentSchedulle = ScheduleParents(task2);
				if(!parentSchedulle.isSuccessful()){
					if(criticalPath.contains(parentSchedulle.getFailTask())){
						constraints.put(parentSchedulle.getFailTask(), parentSchedulle.getSuggestedStartTime());
						removeAllTheCriticalPathTasksFromTheSchedulligs(criticalPath);
						break;
					}else{
						return parentSchedulle;
					}
				}
			}
		}
		return ScheduleParents(task);
	}

	


	private void removeAllTheCriticalPathTasksFromTheSchedulligs(
			ArrayList<Task> criticalPath) {
			for (Iterator<Task> iterator = criticalPath.iterator(); iterator
					.hasNext();) {
				Task task = (Task) iterator.next();
				if (schedulings.containsKey(task)){
					schedulings.remove(task);
				}
			}
	}

	private SchedulleResponse SchedulePath(ArrayList<Task> criticalPath) {
		SchedulleResponse bestSchedulle = null;
		int taskIndex = 0;
		Task t = criticalPath.get(taskIndex);
		//taskIndex++;
		while(t != null){
			int hostIndex = 0;
			Host s = hosts.get(hostIndex);
			if(s == null){
				taskIndex--;
				t = criticalPath.get(taskIndex);
			}else{
				long st = computeST(t,s);
				long c = computeC(t,s);
				if(constraints.containsKey(t)){
					if(st<constraints.get(t));{
						constraints.put(t,st);
					}
				}
				ArrayList<Task> childrenOfT = lookForAllchildrenOfT(t);
				boolean possible = true;
				boolean finished = true;
				int i = 0;
				while(possible&&i<childrenOfT.size()){
					Task child = childrenOfT.get(i);
					if (schedulings.containsKey(child)){
						//if(schedulings.get(child)<) Parei aqui!!
					}
				}
			}
		}
		
		
		
		
		
		
		SchedulleResponse sr = new SchedulleResponse();
		return sr;
	}

	private ArrayList<Task> lookForAllchildrenOfT(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

	private long computeC(Task t, Host s) {
		// TODO Auto-generated method stub
		return 0;
	}

	private long computeST(Task t, Host s) {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean isScheduled(ArrayList<Task> criticalPath) {
		boolean isScheduled = true;
		int i = 0;
		while (i < criticalPath.size() && isScheduled == true) {
			isScheduled = schedulings.containsKey(criticalPath.get(i));
		}
		return isScheduled;
	}

	private boolean hasUnscheduledParents(Task task) {
		boolean hasUnscheduledParents = false;
		int i = 0;
		while (i < task.getDependencies().size()
				&& hasUnscheduledParents == false) {
			hasUnscheduledParents = schedulings.containsKey(task
					.getDependencies().get(i));
		}
		return hasUnscheduledParents;
	}

	private void StartAssignmens() {
		// TODO Auto-generated method stub
	}

	@Override
	public void taskFinished(Task task, Host host)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transmissionFinished(Task task, Host sender, Host destionation)
	{
		// TODO Auto-generated method stub
		
	}

}
