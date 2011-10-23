package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.Calendar;

import org.cloudbus.cloudsim.core.CloudSim;

import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.sim.input.Host;
import br.unicamp.ic.wfscheduler.sim.input.Task;

public class Main
{
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		BrokerImpl broker;
		IScheduler scheduler;
		
		// set scheduler here
		scheduler = new br.unicamp.ic.wfscheduler.impl.random.RandomScheduler();
		
		broker = prepare(args, scheduler);		

		broker.start();
		
		System.out.println("Total cost: " + broker.getTotalCost());
		System.out.println("Total time: " + broker.getTotalTime());
	}
	
	private static BrokerImpl prepare(String[] args, IScheduler scheduler) throws Exception
	{
		long bandwidth = 1000;
		double processingCost = 1;
		ArrayList<Host> hosts = new ArrayList<Host>();
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		hosts.add(new Host(1000, 1));
		tasks.add(new Task(100000, 200));
		
		CloudSim.init(1, Calendar.getInstance(), false);
		
		return new BrokerImpl(hosts, tasks, bandwidth, processingCost, scheduler);
	}
}
