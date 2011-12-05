package br.unicamp.ic.wfscheduler.impl.pcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.unicamp.ic.wfscheduler.*;

public class PartialCriticalPathsScheduler implements IScheduler {
	private List<Task> tasks;
	private List<Host> hosts;
	private long bandwidth;
	private long transmissionCost;

	
	private HashMap<Task, Assignment> schedulings;
	private HashMap<Task, Long> constraints;
	private HashMap<Task, Long> METs;
	private HashMap<Task, Long> MTTs;
	private HashMap<Task, Long> ESTs;
	private HashMap<Host, ArrayList<TimeSlot>> timeSlots;
	
	
	@Override
	public void startScheduler(Broker broker) {
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		bandwidth = broker.getBandwidth();
		long deadline = 999999998;
		scheduleWorkflow(tasks, hosts, deadline);
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
	
	public void initializeTimeSlots(){
		for (Iterator<Host> iterator = hosts.iterator(); iterator.hasNext();) {
			Host h = (Host) iterator.next();
			ArrayList<TimeSlot> slots = new ArrayList<TimeSlot>();
			slots.add(new TimeSlot());
			timeSlots.put(h, slots);
		}
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
			maxEST = findLaziestParentResponse(task, hosts);
		}
		ESTs.put(task, maxEST);
		return maxEST;
	}
	
	private long updateEarliestStartTime(Task task, List<Host> hosts) {
		long maxEST = findLaziestParentResponse(task, hosts);
		ESTs.put(task, maxEST);
		return maxEST;
	}

	private long estimateEarliestFinishTime(Task task,
			List<Host> hosts) {
		long EST = estimateEarliestStartTime(task, hosts);
		long MET = estimateMinimumExecutionTime(task, hosts);
		long MTT = estimateMinimumTransferTime(task, hosts);
		long eft = EST + MET + MTT;
		return eft;
	}
	
	private long findLaziestParentResponse(Task task, List<Host> hosts) {
		long maxEFT = 0;
		for (Iterator<Task> iterator = task.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (!schedulings.containsKey(parent)) {
				long est = estimateEarliestFinishTime(parent, hosts);
				if (maxEFT < est) {
					maxEFT = est;
				}
			}else{
				Assignment assignment = schedulings.get(parent);
				long eft = getFinishTime(parent,assignment);
				if (maxEFT < eft) {
					maxEFT = eft;
				}
			}
		}
		return maxEFT;
	}
	
	

	private long getFinishTime(Task t, Assignment assignment) {
		long et = assignment.getStartTime()+(t.getLength()/assignment.getHost().getProcessingSpeed())+(t.getLength()/bandwidth);
		return et;
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

	private boolean scheduleWorkflow(List<Task> task, List<Host> hosts,
			long deadline) {
		
		boolean sucesso = false;
		schedulings = new HashMap<Task, Assignment>();
		METs = new HashMap<Task, Long>();
		MTTs = new HashMap<Task, Long>();
		ESTs = new HashMap<Task, Long>();

		Task entry = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		Task exit = (Task) new br.unicamp.ic.wfscheduler.sim.input.Task(0, 0);
		tasks = addingTasksEntryAndExit(tasks, entry, exit);
		schedulings.put(entry, new Assignment(null,(long) 0,(long) 0));
		schedulings.put(exit, new Assignment(null,(long) deadline,(long) 0));
		constraints = new HashMap<Task, Long>();
		//	estimateEarliestStartTime(exit, hosts);// apenas para calcular o partial critical path atual
		initializeTimeSlots();
		if (ScheduleParents(exit).isSuccessful()) {
			sucesso = true;
		}
		return sucesso;
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
		SchedulleResponse sr = new SchedulleResponse();
		boolean scheduledFound = false;
		HashMap<Task, Assignment> bestSchedulle = null;
		HashMap<Task, Assignment> currentSchedulle = new HashMap<Task, Assignment>();
		
		HashMap<Task, Integer> currentHostIndex = new HashMap<Task, Integer>();
		/*
		 * O currentHostIndex serve para saber quais os servicos que ja foram tentados para 
		 * aquela tarefa (next untried service ti)
		 */
		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			currentHostIndex.put(task, 0);
		}
		
		
		
		int taskIndex = 0;
		Task t = criticalPath.get(taskIndex);
		while(t != null){
			Host s = hosts.get(currentHostIndex.get(t));
			if(s == null){
				//zera as tentativas, já que vai tentar tudo a partir da tarefa anterior
				//currentHostIndex.put(t, 0);// Será que tem que zerar mesmo? :P
				//volta para a tarefa anterior
				taskIndex--;
				t = criticalPath.get(taskIndex);
			}else{
				long st = computeST(t,s);
				long c = computeC(t,s);
				if(constraints.containsKey(t)){
					if(st<constraints.get(t));{
						st = constraints.get(t);
					}
				}
				ArrayList<Task> childrenOfT = lookForAllchildrenOfT(t);
				boolean possible = true;
				int i = 0;
				long et = t.getLength() / s.getProcessingSpeed();
				while(possible&&i<childrenOfT.size()){
					Task child = childrenOfT.get(i);
					if (schedulings.containsKey(child)){						
						if((st+et+(t.getLength()/bandwidth))>schedulings.get(child).getStartTime()){
							currentHostIndex.put(t, currentHostIndex.get(t)+1);
							possible=false;
							sr.setFailTask(child);
							sr.setSuggestedStartTime(st+et+(t.getLength()/bandwidth));
							sr.setSuccessful(false);
						}
						i++;
					}
				}
				if(possible){
					currentSchedulle.put(t,new Assignment(hosts.get(currentHostIndex.get(t)), st, c));
					if(taskIndex==criticalPath.size()-1){
						scheduledFound = true;
						if(bestSchedulle==null){
							bestSchedulle = (HashMap<Task, Assignment>) currentSchedulle.clone();
							//será q nao deveria voltar em todo caso? nao apenas se fosse melhor q o best schedulle?
							taskIndex--;
							t = criticalPath.get(taskIndex);
						}else{
							if(calculaCusto(currentSchedulle)<calculaCusto(bestSchedulle)){
								bestSchedulle = (HashMap<Task, Assignment>) currentSchedulle.clone();
								taskIndex--;
								t = criticalPath.get(taskIndex);
							}
						}
					}else{
						taskIndex++;
						t = criticalPath.get(taskIndex);
					}
				}else{
					taskIndex--;
					t = criticalPath.get(taskIndex);
				}
			}
		}
		if(scheduledFound){
			sr = new SchedulleResponse();
			sr.setSuccessful(true);
			for (Iterator<Task> iterator = criticalPath.iterator(); iterator
					.hasNext();) {
				Task task = (Task) iterator.next();
				schedulings.put(task, bestSchedulle.get(task));
			}
			updateChildrenESTs(criticalPath);
			return sr;
		}else{
			return sr;
		}
		
	}

	private void updateChildrenESTs(ArrayList<Task> criticalPath) {
		for (Iterator<Task> iterator = criticalPath.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			//long ft = getFinishTime(task, schedulings.get(task));
			ArrayList<Task> children = lookForAllchildrenOfT(task);
			for (Iterator<Task> iterator2 = children.iterator(); iterator2.hasNext();) {
				Task child = (Task) iterator2.next();
				if(!schedulings.containsKey(child)){
					updateEarliestStartTime(child, hosts);
				}
			}
		}		
	}

	private long calculaCusto(HashMap<Task, Assignment> currentSchedulle) {
		Set<Task> TodasAsTasks = currentSchedulle.keySet();
		long cost = 0;
		for (Task task : TodasAsTasks) {
			cost = cost+currentSchedulle.get(task).getCost();
		}
		return cost;
	}

	private ArrayList<Task> lookForAllchildrenOfT(Task t) {
		ArrayList<Task> children = new ArrayList<Task>();
		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			if(task.getDependencies().contains(t)){
				children.add(task);
			}
		}
		return children;
	}

	private long computeC(Task t, Host s) {
		long exCost = (long) (t.getLength()*s.getCost());
		long parentsTransferCost = 0;
		long childTransferCost = 0;
		
		for (Iterator<Task> iterator = t.getDependencies().iterator(); iterator.hasNext();) {
			Task parent = (Task) iterator.next();
			if(schedulings.containsKey(parent)){
				parentsTransferCost += parent.getLength()*transmissionCost;
			}
		}
		ArrayList<Task> children = lookForAllchildrenOfT(t);
		for (Iterator<Task> iterator = children.iterator(); iterator.hasNext();) {
			Task child = (Task) iterator.next();
			if(schedulings.containsKey(child)){
				childTransferCost+= t.getLength()*transmissionCost;
			}
		}
		
		return exCost+parentsTransferCost+childTransferCost;
	}

	private long computeST(Task t, Host s) {
		boolean found = false;
		long minST = estimateStartTime(t,s);
		long et = t.getLength()/s.getProcessingSpeed();
		ArrayList<TimeSlot> slots = timeSlots.get(s);
		int i = 0;
		while(!found && i<slots.size()){
			TimeSlot slot = slots.get(i);
			if(slot.getStartTime()<=minST){
				if(slot.getFinishTime()==-1||slot.getFinishTime()>=minST+et){
					found = true;
					TimeSlot antes = new TimeSlot(slot.getStartTime(),minST);
					TimeSlot depois = new TimeSlot(minST+et,slot.getFinishTime());
					slots.remove(i);
					if(depois.getFinishTime()-depois.getStartTime()!=0){
						slots.add(depois);
					}
					if(antes.getFinishTime()-antes.getStartTime()!=0){
						slots.add(antes);
					}
				}
			}else{
				if(slot.getFinishTime()==-1||slot.getFinishTime()-slot.getStartTime()>=et){
					found = true;
					TimeSlot depois = new TimeSlot(slot.getStartTime()+et,slot.getFinishTime());
					slots.remove(i);
					if(depois.getFinishTime()-depois.getStartTime()!=0){
						slots.add(depois);
					}
					minST = slot.getStartTime();
				}
			}
		}
		return minST;
	}

	private long estimateStartTime(Task t, Host s) {
		long maxEFT = 0;
		for (Iterator<Task> iterator = t.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (schedulings.containsKey(parent)) {
				Assignment assignment = schedulings.get(parent);
				long eft = getFinishTime(parent,assignment);
				if (maxEFT < eft) {
					maxEFT = eft;
				}
			}
		}
		return maxEFT;
		
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



}
