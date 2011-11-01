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
	private HashMap<Task, Long> constraints;
	private HashMap<Task, Assignment> assignments;
	private HashMap<Task, Long> schedulings;
	private HashMap<Task, Long> METs;
	private HashMap<Task, Long> MTTs;
	private HashMap<Task, Long> ESTs;

	@Override
	public void startScheduler(Broker broker) {
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		bandwidth = broker.getBandwidth();
		long deadline = 999999998;
		scheduleWorkflow(tasks, hosts,deadline);
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

	private long estimateEarliestExecutionTime(Task task, List<Host> hosts) {
		long maxEST = 0;
		if (ESTs.containsKey(task)) {
			maxEST = ESTs.get(task);
		} else {
			Task criticalParent = findCriticalParent(task, hosts);
			maxEST = estimateEarliestExecutionTime(criticalParent, hosts);
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
				long parentEST = estimateEarliestExecutionTime(parent, hosts);
				long parentMET = estimateMinimumExecutionTime(parent, hosts);
				long parentMTT = estimateMinimumTransferTime(parent, hosts);
				long est = parentEST + parentMET + parentMTT;
				if (maxEST < est) {
					criticalParent = parent;
					maxEST = est;
				}
			}
		}
		return criticalParent;
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
		METs= new HashMap<Task, Long>();
		MTTs= new HashMap<Task, Long>();
		ESTs= new HashMap<Task, Long>();
		
		Task entry = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		Task exit = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		tasks = addingTasksEntryAndExit(tasks, entry, exit);
		schedulings.put(entry, (long) 0);
		schedulings.put(exit, (long) deadline);
		estimateEarliestExecutionTime(exit, hosts);
		if (ScheduleParents(exit)) {
			StartAssignmens();
		}
	}

	private boolean ScheduleParents(Task task) {
		if (!hasUnscheduledParents(task)) {
			return true;
		}
		Task taskI = task;
		ArrayList<Task> criticalPath = new ArrayList<Task>();
		while(hasUnscheduledParents(taskI)){
			Task criticalParent = findCriticalParent(taskI, hosts);
			criticalPath.add(0, criticalParent);
			taskI = criticalParent;
		}
		constraints = new HashMap<Task, Long>();
		while(!criticalPath.isEmpty()){
			//PAREI AQUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}
		return false;
	}

	private boolean hasUnscheduledParents(Task task) {
		boolean hasUnscheduledParents = false;
		int i = 0;
		while (i < task.getDependencies().size()
				&& hasUnscheduledParents == false) {
			hasUnscheduledParents = schedulings.containsKey(task.getDependencies().get(i));
		}
		return hasUnscheduledParents;
	}

	private void StartAssignmens() {
		// TODO Auto-generated method stub
	}

}
