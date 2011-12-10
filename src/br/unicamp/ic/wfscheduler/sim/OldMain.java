package br.unicamp.ic.wfscheduler.sim;


import java.util.ArrayList;
import java.util.Calendar;

import br.unicamp.ic.wfscheduler.impl.pcp.PartialCriticalPathsScheduler;


import java.util.ArrayList;
import java.util.Calendar;

import org.cloudbus.cloudsim.core.CloudSim;

import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.impl.pcp.PartialCriticalPathsScheduler;
import br.unicamp.ic.wfscheduler.sim.input.Host;
import br.unicamp.ic.wfscheduler.sim.input.Task;

public class OldMain {

	public static void main(String[] args) throws Exception
	{
		BrokerImpl broker;
		IScheduler scheduler;
		IScheduler PCPscheduler;
		
		// set scheduler here
		//scheduler = new br.unicamp.ic.wfscheduler.impl.random.RandomScheduler();
			
		//broker = prepare(args, scheduler);		

		//broker.start();
		
		//System.out.println("Total cost: " + broker.getTotalCost());
		//System.out.println("Total time: " + broker.getTotalTime());
		
		
		PCPscheduler = new PartialCriticalPathsScheduler();
		
		broker = preparePCPuseCase(args, PCPscheduler);
		
		broker.start();
		
		//System.out.println("Total cost: " + broker.getTotalCost());
		//System.out.println("Total time: " + broker.getTotalTime());
		
	}
	
	private static BrokerImpl prepare(String[] args, IScheduler scheduler) throws Exception
	{
		long bandwidth = 1000;
		double processingCost = 0.25;
		ArrayList<Host> hosts = new ArrayList<Host>();
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		hosts.add(new Host(1000, 1,0.1));
		hosts.add(new Host(1000, 1,0.2));
		hosts.add(new Host(1000, 1,0.3));
		tasks.add(new Task(25000, 20000));
		tasks.add(new Task(10000, 10000));
		tasks.add(new Task(25000, 10000));
		
		tasks.get(2).addDependencies(tasks.get(0));
		tasks.get(2).addDependencies(tasks.get(1));
		
		CloudSim.init(1, Calendar.getInstance(), false);
		return new BrokerImpl(hosts, tasks, bandwidth, processingCost, scheduler);
	}
	
	private static BrokerImpl preparePCPuseCase(String[] args, IScheduler scheduler) throws Exception
	{
		long bandwidth = 1000;
		double processingCost = 0.25;
		ArrayList<Host> hosts = new ArrayList<Host>();
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		hosts.add(new Host(1000, 1,0.1));
		hosts.add(new Host(1000, 1,0.01));
		hosts.add(new Host(1000, 1,0.01));
		hosts.add(new Host(1000, 1,0.1));
		
		Task t1 = new Task(20001,10000);
		Task t2 = new Task(60002,20000);
		Task t3 = new Task(20003,10000);
		Task t4 = new Task(20004,10000);
		Task t5 = new Task(20005,10000);
		Task t6 = new Task(80006,20000);
		Task t7 = new Task(20007,10000);
		Task t8 = new Task(20008,10000);
		Task t9 = new Task(60009,20000);
//		Task tentry = new Task(0,0);
//		Task texit = new Task(0,0);
		
		t9.addDependencies(t8);
		t9.addDependencies(t6);
		t8.addDependencies(t5);
		t7.addDependencies(t5);
		t7.addDependencies(t4);
		t6.addDependencies(t3);
		t6.addDependencies(t2);
		t5.addDependencies(t2);
		t4.addDependencies(t1);
//		t3.addDependencies(tentry);
//		t2.addDependencies(tentry);
//		t1.addDependencies(tentry);
//		texit.addDependencies(t9);
//		texit.addDependencies(t8);
//		texit.addDependencies(t7);
		
//		tasks.add(tentry);
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
		tasks.add(t4);
		tasks.add(t5);
		tasks.add(t6);
		tasks.add(t7);
		tasks.add(t8);
		tasks.add(t9);
//		tasks.add(texit);
		
		double deadline = 100000000;
		CloudSim.init(1, Calendar.getInstance(), false);
		return new BrokerImpl(hosts, tasks, bandwidth, deadline, scheduler);
		//return new BrokerImpl(hosts, tasks, bandwidth, processingCost, scheduler);
	}


}
