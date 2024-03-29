package br.unicamp.ic.wfscheduler.impl.pcp;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.unicamp.ic.wfscheduler.*;

public class PartialCriticalPathsScheduler implements IScheduler {
	private List<Task> tasks;
	Task exit;
	private List<Host> hosts;
	private Broker broker;
	private double bandwidth;
	private double transmissionCost;
	private HashMap<Task, Assignment> schedulings;
	private HashMap<Task, ArrayList<Task>> paisTerminados;
	private HashMap<Task, ArrayList<Task>> childrenOf;
	private HashMap<Task, Double> constraints;
	private double deadline;
	private HashMap<Task, Double> METs;
	private HashMap<Task, Double> MTTs;
	private HashMap<Task, Double> ESTs;
	private HashMap<Host, ArrayList<TimeSlot>> timeSlots;
	
	
	@Override
	public void startScheduler(Broker broker) {
		paisTerminados = new HashMap<Task, ArrayList<Task>>();
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		bandwidth = broker.getBandwidth();
		deadline = broker.getDeadline();
		transmissionCost = 0;
		this.broker = broker;
		if(scheduleWorkflow()){
			calculaTodosOsFilhos();
			imprimefilhos();
			System.out.println(printSchedule(schedulings));
			assignNonDependentTasks();
		}else{
			System.out.println("--------------------------------------");
			System.out.println("IMPOSSIVEL ESCALONAR COM ESSE DEADLINE");
			System.out.println("--------------------------------------");
		}
 

	}


	private void assignNonDependentTasks() {
		ArrayList<Task> noDependecies = new ArrayList<Task>();
		for (Task task : tasks) {
			if (!task.hasDependencies()){
				noDependecies.add(task);
			}
		}
		ArrayList<TaskAssigned> tas = new ArrayList<TaskAssigned>();
		for (Task tfree : noDependecies) {
			TaskAssigned ta = new TaskAssigned(tfree, schedulings.get(tfree));
			tas.add(ta);
		}
		Collections.sort(tas,assgnmentsComparator);
		for (TaskAssigned taskAssigned : tas) {
			Task t = taskAssigned.getTask();
			broker.assign(t, schedulings.get(t).getHost());
		}
	}


	private void eliminateEntryAndExit(Task entry, Task exit) {
		for (Task task : tasks) {
			if(task.getDependencies().contains(entry)){
				task.getDependencies().remove((entry));
			}
		}
		tasks.remove(entry);
		tasks.remove(exit);
		schedulings.remove(entry);
		schedulings.remove(exit);
	}


	private void calculaTodosOsFilhos() {
		childrenOf = new HashMap<Task, ArrayList<Task>>();
		for (Task task : tasks) {
			for (Task parent : task.getDependencies()) {
				ArrayList<Task> filhos = childrenOf.get(parent);
				if(filhos==null){
					filhos = new ArrayList<Task>();
				}
				filhos.add(task);
				childrenOf.put(parent, filhos);
			}
		}
	}


	@Override
	public void taskFinished(Task task, Host host)
	{
		System.out.println("TAREFA FINALIZADA: "+task.getID());
		//ArrayList<Host> hosts = new ArrayList<Host>();
		ArrayList<Task> cs = childrenOf.get(task);
		ArrayList<TaskAssigned> childrenAssigned = new ArrayList<TaskAssigned>();
		
		if(cs!=null){
			for (Task c : cs) {
				TaskAssigned ta = new TaskAssigned(c, schedulings.get(c));
				childrenAssigned.add(ta);
			}
			Collections.sort(childrenAssigned,assgnmentsComparator);
		
		
			for (TaskAssigned childAssigned : childrenAssigned){
				Task child = childAssigned.getTask();
				ArrayList<Task> paisFinished;
				if(paisTerminados.containsKey(child)){
					paisFinished = paisTerminados.get(child);
				}else{
					paisFinished = new ArrayList<Task>();
				}
				paisFinished.add(task);
				paisTerminados.put(child,paisFinished);
				boolean todosOsPaisTerminados = true;
				for (Task dep : child.getDependencies()) {
					if(!paisTerminados.get(child).contains(dep)){
						todosOsPaisTerminados =false;
					}
				}
				if(todosOsPaisTerminados){
					System.out.println("     TAREFA LIBERADA: "+child.getID());
					ArrayList<Host> trans = new ArrayList<Host>();
					trans.add(schedulings.get(child).getHost());
				
					broker.assign(child, schedulings.get(child).getHost());
					for 	(Task dep : child.getDependencies()) {
						broker.transmitResult(dep, trans);	
					}	

				}
			}
		}
	}

	@Override
	public void transmissionFinished(Task task, Host sender, Host destionation)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void initializeTimeSlots(){
		timeSlots = new HashMap<Host, ArrayList<TimeSlot>>();
		for (Iterator<Host> iterator = hosts.iterator(); iterator.hasNext();) {
			Host h = (Host) iterator.next();
			ArrayList<TimeSlot> slots = new ArrayList<TimeSlot>();
			slots.add(new TimeSlot());
			timeSlots.put(h, slots);
		}
	}
	
	private double estimateMinimumTransferTime(Task task, List<Host> hosts) {
		// We are considering uniform bandwidth
		double mtt;
		if (MTTs.containsKey(task)) {
			mtt = MTTs.get(task);
		} else {
			mtt = task.getOutputSize() / bandwidth;
			MTTs.put(task, mtt);
		}
		return mtt;
	}

	private double estimateMinimumExecutionTime(Task task, List<Host> hosts) {
		double met = -1;
		if (METs.containsKey(task)) {
			met = METs.get(task);
		} else {
			for (Iterator<Host> iterator = hosts.iterator(); iterator.hasNext();) {
				Host host = (Host) iterator.next();
				double et = task.getLength() / host.getProcessingSpeed();
				if (met == -1)
					met = et;
				else if (met > et)
					met = et;
			}
		}
		METs.put(task, met);
		return met;
	}

	private double estimateEarliestStartTime(Task task, List<Host> hosts) {
		double maxEST = 0;
		if (ESTs.containsKey(task)) {
			maxEST = ESTs.get(task);
		} else {
			maxEST = findLaziestParentResponse(task, hosts);
		}
		ESTs.put(task, maxEST);
		return maxEST;
	}
	
	private double updateEarliestStartTime(Task task, List<Host> hosts) {
		double maxEST = findLaziestParentResponse(task, hosts);
		ESTs.put(task, maxEST);
		return maxEST;
	}

	private double estimateEarliestFinishTime(Task task,
			List<Host> hosts) {
		double EST = estimateEarliestStartTime(task, hosts);
		double MET = estimateMinimumExecutionTime(task, hosts);
		double MTT = estimateMinimumTransferTime(task, hosts);
		double eft = EST + MET + MTT;
		return eft;
	}
	
	private double findLaziestParentResponse(Task task, List<Host> hosts) {
		double maxEFT = 0;
		for (Iterator<Task> iterator = task.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (!schedulings.containsKey(parent)) {
				double est = estimateEarliestFinishTime(parent, hosts);
				if (maxEFT < est) {
					maxEFT = est;
				}
			}else{
				Assignment assignment = schedulings.get(parent);
				double eft = getFinishTime(parent,assignment);
				if (maxEFT < eft) {
					maxEFT = eft;
				}
			}
		}
		return maxEFT;
	}
	
	

	private double getFinishTime(Task t, Assignment assignment) {
		double et = assignment.getStartTime()+(t.getLength()/assignment.getHost().getProcessingSpeed())+(t.getOutputSize()/bandwidth);
		return et;
	}


	private void addingTasksEntryAndExit(Task entry,Task exit) {

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
		
		tasks.add(entry);
		tasks.add(exit);
	}

	private boolean scheduleWorkflow() {
		
		boolean sucesso = false;
		schedulings = new HashMap<Task, Assignment>();
		METs = new HashMap<Task, Double>();
		MTTs = new HashMap<Task, Double>();
		ESTs = new HashMap<Task, Double>();
		Task entry = new DummyTask();
		exit = new DummyTask();
		addingTasksEntryAndExit(entry, exit);
		schedulings.put(entry, new Assignment(null,(double) 0,(double) 0));
		schedulings.put(exit, new Assignment(null,(double) deadline,(double) 0));
		constraints = new HashMap<Task, Double>();
		estimateEarliestStartTime(exit, hosts);
		System.out.println(ESTs.get(exit));
		
		initializeTimeSlots();
		calculaTodosOsFilhos();
		imprimefilhos();
		if (ScheduleParents(exit).isSuccessful()) {
			sucesso = true;
		}
		eliminateEntryAndExit(entry,exit);
		return sucesso;
	}
	
//	private SchedulleResponse ScheduleParents2(Task task) {
//		if (!hasUnscheduledParents(task)) {
//			SchedulleResponse sr = new SchedulleResponse();
//			sr.setSuccessful(true);
//			return sr;
//		}else{
//			Task taskI = task;
//			ArrayList<Task> criticalPath = new ArrayList<Task>();
//			while (hasUnscheduledParents(taskI)) {
//				Task criticalParent = findCriticalParent(taskI, hosts);
//				criticalPath.add(0, criticalParent);
//				taskI = criticalParent;
//			}
//		}
//	}
	
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
			SchedulleResponse criticalPathSchedule = SchedulePathRecursiveHelper(criticalPath);
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
		double maxEST = -1;
		Task criticalParent = null;
		for (Iterator<Task> iterator = task.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (!schedulings.containsKey(parent)) {
				double est = estimateEarliestFinishTime(parent, hosts);
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
	
	private SchedulleResponse SchedulePathRecursiveHelper(ArrayList<Task> criticalPath) {
		
		HashMap<Task, Assignment> currentSchedulle = new HashMap<Task, Assignment>();
		HashMap<Host, ArrayList<TimeSlot>> currentTimeSlots = (HashMap<Host, ArrayList<TimeSlot>>) timeSlots.clone();
		System.out.println("-----------------------------------------------------");
		System.out.println("CRITICAL PATH: "+printPath(criticalPath));
		System.out.println("-----------------------------------------------------");
//		System.out.println(" Task              Host ");
//		System.out.println("------------------------");
		SchedulleResponse sr = SchedulePathRecursive(criticalPath, currentSchedulle, currentTimeSlots);
		if (sr.isSuccessful()){
//			System.out.println("------------------------");
			System.out.println("CRITICAL PATH ESCALONADO");
			System.out.println(printSchedule(sr.getSchedulle()));
			for (Iterator<Task> iterator = criticalPath.iterator(); iterator.hasNext();) {
				Task task = (Task) iterator.next();
				schedulings.put(task, sr.getSchedulle().get(task));
			}
			updateChildrenESTs(criticalPath);
		}else{
			System.out.println("SCHEDDULE FAIL! TASK: "+sr.getFailTask().getLength());
		}
		return sr;
	}
	
	


	private SchedulleResponse SchedulePathRecursive(ArrayList<Task> cPath, HashMap<Task, Assignment> cSchedulle,HashMap<Host, ArrayList<TimeSlot>> ScheduletimeSlots) {
		HashMap<Task, Assignment> bestSchedulle = null;
		HashMap<Host, ArrayList<TimeSlot>> tslots = (HashMap<Host, ArrayList<TimeSlot>>)ScheduletimeSlots.clone();
		ArrayList<Task> criticalPath = (ArrayList<Task>) cPath.clone();
		HashMap<Task, Assignment> currentSchedulle = (HashMap<Task, Assignment>) cSchedulle.clone();
		
		SchedulleResponse sr = new SchedulleResponse();
		sr.setSuccessful(true);
		Task t = criticalPath.remove(0);
		for (Host s : hosts) {
//			System.out.println(t.getLength()+"        "+s.getID());
			double st = computeSTconstraints(t,s,currentSchedulle,tslots);
			double c = computeC(t,s,currentSchedulle);
			// na verdade os constraints devem ser passados para o computeST, constraints s�o limites inferiores para iniciar a simula��o
			sr = lookForChildrenDependencies(t,s,st);
			if(sr.isSuccessful()){
				currentSchedulle.put(t,new Assignment(s, st, c));
				if(criticalPath.size()>0){
					sr = SchedulePathRecursive(criticalPath, currentSchedulle, ScheduletimeSlots);
				}else{
					sr.setSchedulle(currentSchedulle);
//					System.out.println("Schedule found! :"+printSchedule(sr.getSchedulle()));
				}
//				System.out.println("task "+t.getLength()+" Testing Schedule: ");
				if(sr.getSchedulle()!=null&&sr.isSuccessful()){

					if(bestSchedulle==null){
//						System.out.println("     best so far: "+printSchedule(sr.getSchedulle()));
						bestSchedulle = (HashMap<Task, Assignment>) sr.getSchedulle().clone();					
					}else{					
						if(calculaCusto(sr.getSchedulle())<calculaCusto(bestSchedulle)){
//							System.out.println("   its the best! "+printSchedule(sr.getSchedulle()));
							bestSchedulle = (HashMap<Task, Assignment>) sr.getSchedulle().clone();
						}else{
//							System.out.println("     not the best");
//							System.out.println("         best one: "+calculaCusto(bestSchedulle));
//							System.out.println("          current: "+calculaCusto(sr.getSchedulle()));
						}
					}
				} 
				
			}
		}
//		System.out.println(" task "+t.getLength()+" retornando bs: "+calculaCusto(bestSchedulle));
		if(bestSchedulle!=null){
			sr = new SchedulleResponse();
			sr.setSuccessful(true);
			sr.setSchedulle(bestSchedulle);
		}
		return sr;
	}
	
	private String printPath(ArrayList<Task> criticalPath) {
		String s = "";
		for (Task task : criticalPath) {
			s+=" "+task.getLength();
		}
		return s;
	}
	
	
	private String printSchedule(HashMap<Task, Assignment> Schedulle){
		Set<Task> keys = Schedulle.keySet();
		String s = "     Task  Host"+"\n";
		for (Task task : keys) {
			s = s+"       "+task.getLength()+"  "+Schedulle.get(task).getHost().getID()+"\n";
		}
		s = s+"          custo: "+calculaCusto(Schedulle);
		
		return s;
	}
	private double computeSTconstraints(Task t, Host s,HashMap<Task, Assignment> currentSchedulle, HashMap<Host, ArrayList<TimeSlot>> ScheduletimeSlots) {
		boolean found = false;
		double minST = estimateStartTimeConstraints(t,s,currentSchedulle);
		double et = t.getLength()/s.getProcessingSpeed();
		ArrayList<TimeSlot> slots = ScheduletimeSlots.get(s);
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
				} else{
					i++;
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
				} else{
					i++;
				}
			}
		}
		return minST;
	}
	
	private double estimateStartTimeConstraints(Task t, Host s, HashMap<Task, Assignment> currentSchedulle) {
		double maxEFT = 0;
		for (Iterator<Task> iterator = t.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (schedulings.containsKey(parent)) {
				Assignment assignment = schedulings.get(parent);
				double eft = getFinishTime(parent,assignment);
				if (maxEFT < eft) {
					maxEFT = eft;
				}
			}
			else
				if(currentSchedulle.containsKey(parent)){
				Assignment assignment = currentSchedulle.get(parent);
				double eft = getFinishTime(parent,assignment);
				if (maxEFT < eft) {
					maxEFT = eft;
				}
			}
		}
		if(constraints.containsKey(t)){
			if(maxEFT<constraints.get(t)){
				maxEFT = constraints.get(t);
			}	
		}
		return maxEFT;
	}
	
	private SchedulleResponse lookForChildrenDependencies(Task t,Host s,double st){
		SchedulleResponse sr = new SchedulleResponse();
		sr.setSuccessful(true);
		int i = 0;
		double et = t.getLength() / s.getProcessingSpeed();
		ArrayList<Task> childrenOfT = childrenOf.get(t);
		while(sr.isSuccessful()&&i<childrenOfT.size()){
		Task child = childrenOfT.get(i);
		if (schedulings.containsKey(child)){			
			if((st+et+(t.getOutputSize()/bandwidth))>schedulings.get(child).getStartTime()){
				sr.setFailTask(child);
				sr.setSuggestedStartTime(st+et+(t.getOutputSize()/bandwidth));
				sr.setSuccessful(false);
				return sr;
			}else{
				sr.setSuccessful(true);
			}
		}
		i++;
		}
		return sr;
	}
	
	
/*
	private SchedulleResponse SchedulePath(ArrayList<Task> criticalPath) {
		SchedulleResponse sr = new SchedulleResponse();
		boolean scheduledFound = false;
		HashMap<Task, Assignment> bestSchedulle = null;
		HashMap<Task, Assignment> currentSchedulle = new HashMap<Task, Assignment>();
		
		HashMap<Task, Integer> currentHostIndex = new HashMap<Task, Integer>();
		
		 // O currentHostIndex serve para saber quais os servicos que ja foram tentados para 
		 // aquela tarefa (next untried service ti)
		 
		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			currentHostIndex.put(task, -1);
		}
		
		
		
		int taskIndex = 0;
		System.out.println("Task        Host");
		System.out.println("-------------------------");
		Task t = criticalPath.get(taskIndex);
		while(taskIndex != -1){
			currentHostIndex.put(t, currentHostIndex.get(t)+1);
			int chi = currentHostIndex.get(t);
			
			
			System.out.println(taskIndex+"                 "+chi);
			
			
			if(chi >= hosts.size()||chi <0){
				//zera as tentativas, j� que vai tentar tudo a partir da tarefa anterior
				//currentHostIndex.put(t, 0);// Ser� que tem que zerar mesmo? :P
				//volta para a tarefa anterior
				taskIndex--;
				if (taskIndex >=0 ){
					t = criticalPath.get(taskIndex);
				}
			}else{
				Host s = hosts.get(currentHostIndex.get(t));
				double st = computeST(t,s,currentSchedulle);
				double c = computeC(t,s);
				// na verdade os constraints devem ser passados para o computeST, constraints s�o limites inferiores para iniciar a simula��o
				if(constraints.containsKey(t)){
					if(st<constraints.get(t));{
						st = constraints.get(t);
					}
				}
				ArrayList<Task> childrenOfT = lookForAllchildrenOfT(t);
				boolean possible = true;
				int i = 0;
				double et = t.getLength() / s.getProcessingSpeed();
				while(possible&&i<childrenOfT.size()){
					Task child = childrenOfT.get(i);
					if (schedulings.containsKey(child)){						
						if((st+et+(t.getLength()/bandwidth))>schedulings.get(child).getStartTime()){
							possible=false;
							sr.setFailTask(child);
							sr.setSuggestedStartTime(st+et+(t.getLength()/bandwidth));
							sr.setSuccessful(false);
						}
					}
					i++;
				}
				if(possible){
					currentSchedulle.put(t,new Assignment(hosts.get(currentHostIndex.get(t)), st, c));
					if(taskIndex==criticalPath.size()-1){
						scheduledFound = true;
						if(bestSchedulle==null){
							bestSchedulle = (HashMap<Task, Assignment>) currentSchedulle.clone();
							//ser� q nao deveria voltar em todo caso? nao apenas se fosse melhor q o best schedulle?
							taskIndex--;
							if (taskIndex >=0 ){
								t = criticalPath.get(taskIndex);
							}
						}else{
							if(calculaCusto(currentSchedulle)<calculaCusto(bestSchedulle)){
								bestSchedulle = (HashMap<Task, Assignment>) currentSchedulle.clone();
								taskIndex--;
								if (taskIndex >=0 ){
									t = criticalPath.get(taskIndex);
								}
							}
						}
					}else{
						taskIndex++;
						t = criticalPath.get(taskIndex);
					}
				}else{
					taskIndex--;
					if (taskIndex >=0 ){
						t = criticalPath.get(taskIndex);
					}
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
*/
	
	private void updateChildrenESTs(ArrayList<Task> criticalPath) {
		for (Iterator<Task> iterator = criticalPath.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			//double ft = getFinishTime(task, schedulings.get(task));
			ArrayList<Task> children = childrenOf.get(task);
			//ArrayList<Task> children = lookForAllchildrenOfT(task);
			for (Iterator<Task> iterator2 = children.iterator(); iterator2.hasNext();) {
				Task child = (Task) iterator2.next();
				if(!schedulings.containsKey(child)){
					updateEarliestStartTime(child, hosts);
				}
			}
		}		
	}

	private double calculaCusto(HashMap<Task, Assignment> currentSchedulle) {
		Set<Task> TodasAsTasks = currentSchedulle.keySet();
		double cost = 0;
		for (Task task : TodasAsTasks) {
			cost = cost+currentSchedulle.get(task).getCost();
		}
		return cost;
	}

/*	private ArrayList<Task> lookForAllchildrenOfT(Task t) {
		ArrayList<Task> children = new ArrayList<Task>();
		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			if(task.getDependencies().contains(t)){
				children.add(task);
			}
		}
		return children;
	}
*/
	private double computeC(Task t, Host s, HashMap<Task, Assignment> currentSchedulle) {
		double exCost = (double) (t.getLength()*s.getCost());
		double parentsTransferCost = 0;
		double childTransferCost = 0;
		
		for (Iterator<Task> iterator = t.getDependencies().iterator(); iterator.hasNext();) {
			Task parent = (Task) iterator.next();
			if(schedulings.containsKey(parent)){
				if(schedulings.get(parent).getHost().getID()!=s.getID())
				parentsTransferCost += parent.getOutputSize()*transmissionCost;
			} else if(currentSchedulle.containsKey(parent)){
				if(currentSchedulle.get(parent).getHost().getID()!=s.getID())
				parentsTransferCost += parent.getOutputSize()*transmissionCost;
			}
		}
		ArrayList<Task> children = childrenOf.get(t);
		//ArrayList<Task> children = lookForAllchildrenOfT(t);
		for (Iterator<Task> iterator = children.iterator(); iterator.hasNext();) {
			Task child = (Task) iterator.next();
			if(schedulings.containsKey(child)){
				childTransferCost+= t.getOutputSize()*transmissionCost;
			}
		}
		
		return exCost+parentsTransferCost+childTransferCost;
	}

	private double computeST(Task t, Host s,HashMap<Task, Assignment> currentSchedulle) {
		boolean found = false;
		double minST = estimateStartTime(t,s,currentSchedulle);
		double et = t.getLength()/s.getProcessingSpeed();
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
				} else{
					i++;
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
				} else{
					i++;
				}
			}
		}
		return minST;
	}

	private double estimateStartTime(Task t, Host s, HashMap<Task, Assignment> currentSchedulle) {
		double maxEFT = 0;
		for (Iterator<Task> iterator = t.getDependencies().iterator(); iterator
				.hasNext();) {
			Task parent = (Task) iterator.next();
			if (schedulings.containsKey(parent)) {
				Assignment assignment = schedulings.get(parent);
				double eft = getFinishTime(parent,assignment);
				if (maxEFT < eft) {
					maxEFT = eft;
				}
			}else if(currentSchedulle.containsKey(parent)){
				Assignment assignment = currentSchedulle.get(parent);
				double eft = getFinishTime(parent,assignment);
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
			i++;
		}
		return isScheduled;
	}

	private boolean hasUnscheduledParents(Task task) {
		boolean hasUnscheduledParents = false;
		int i = 0;
		while (i < task.getDependencies().size() && hasUnscheduledParents == false) {
			hasUnscheduledParents = !schedulings.containsKey(task.getDependencies().get(i));
			i++;
		}
		return hasUnscheduledParents;
	}

	
	Comparator<TaskAssigned> assgnmentsComparator = new Comparator<TaskAssigned>() {
		
		@Override
		public int compare(TaskAssigned o1, TaskAssigned o2) {
			if(o1.getAssignment().getStartTime()<o2.getAssignment().getStartTime()) return -1;
			else if(o1.getAssignment().getStartTime()>o2.getAssignment().getStartTime()) return 1;
			else return 0;
		}
	};
	private void imprimefilhos(){
		Set<Task> ts = childrenOf.keySet();
		for (Task task : ts) {
			System.out.println("Task:"+task.getLength());
			System.out.println("  Filhos:");
			ArrayList<Task> filhos = childrenOf.get(task);
			for (Task task2 : filhos) {
				System.out.println("           "+task2.getLength());
			}
		}
		
	}
	
/*	private void createTaskAssignments() {
		taskAssigneds = new ArrayList<TaskAssigned>();
		Set<Task> ts = schedulings.keySet();
		for (Task task : ts) {
			TaskAssigned ta = new TaskAssigned(task, schedulings.get(task));
		}
		Collections.sort(taskAssigneds,assgnmentsComparator);
	}*/


}
