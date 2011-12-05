package br.unicamp.ic.wfscheduler.sim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import org.cloudbus.cloudsim.core.CloudSim;

import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.impl.random.RandomScheduler;
import br.unicamp.ic.wfscheduler.impl.roundrobin.RoundRobinScheduler;
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
		Main m;
		BrokerImpl broker;
		IScheduler scheduler;
		String schedulerName;
		
		if (args.length != 4)
		{
			printHelp();
			System.exit(-1);
		}
		
		schedulerName = args[3];
		
		if (schedulerName.equals("random"))
			scheduler = new RandomScheduler();
		else if (schedulerName.equals("roundrobim"))
			scheduler = new RoundRobinScheduler();
		else if (schedulerName.equals("pso"))
			scheduler = new RoundRobinScheduler();
		else if (schedulerName.equals("pcp"))
			scheduler = new RoundRobinScheduler();
		else
		{
			System.err.println("Invalid scheduler name");
			printHelp();
			scheduler = null;
			System.exit(-1);
		}
				
		broker = prepare(args, scheduler);		

		System.err.println("Starting simulation");
		
		broker.start();
		
		System.err.println("Simulation finished");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));

		writer.write("# Total cost / total time\n");
		writer.write(String.valueOf(broker.getTotalCost()) + "\n");
		writer.write(String.valueOf(broker.getTotalTime()) + "\n");
		
		writer.close();
	}
	
	private static void printHelp()
	{
		System.err.println("./wfscheduler hostfile taskfile outputfile scheduler_name");
		System.err.println("Possible scheduler names are: random, roundrobim, pso, pcp");
	}
	
	private static ArrayList<Task> parseTasks(String taskPath)
	{		
		int nodeNum;
		String buf;
		String n;
		String val;
		int index;
		int index2;
		
		ArrayList<Task> tasks = new ArrayList<Task>();		
				
		try
		{
			File f = new File(taskPath);
			Scanner reader = new Scanner(f);
			
			nodeNum = Integer.parseInt(reader.nextLine());
			
			for (int i = 0; i < nodeNum; i++)
			{
				buf = reader.nextLine();
				
				index = buf.indexOf(" : ");
				n = buf.substring(0, index );
				val = buf.substring(index + 3);
				
				tasks.add(new Task(Integer.parseInt(val), 0));
			}
			
			// read arcs
			
			while (reader.hasNextLine())
			{	
				buf = reader.nextLine();
				
				Task t, d;
				
				index = buf.indexOf(" -> ");
				index2 = buf.indexOf(" : ");
				
				t = tasks.get( Integer.parseInt(buf.substring(0, index )) - 1 );
				d = tasks.get( Integer.parseInt(buf.substring(index + 4, index2)) - 1);
				
				t.setOutputSize((long)Double.parseDouble(buf.substring(index2 + 3)));
				d.addDependencies(t);
			}
			
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		return tasks;
	}
	
	private static BrokerImpl prepare(String[] args, IScheduler scheduler) throws Exception
	{
		long bandwidth;
		double processingCost;
		String hostFile;
		String buf;
		ArrayList<Host> hosts;
		
		CloudSim.init(1, Calendar.getInstance(), false);
		
		hostFile = args[0];
		
		try
		{
			File f = new File(hostFile);
			Scanner reader = new Scanner(f);
			
			// file format
			// bandwidth
			// computation cost
			
			// numer_of_cores mips
			
			bandwidth = Integer.parseInt(reader.nextLine());
			processingCost = Double.parseDouble(reader.nextLine());
			
			hosts = new ArrayList<Host>();
			
			while (reader.hasNextInt())
			{
				int proc = reader.nextInt();
				long mips = reader.nextLong();
				hosts.add(new Host(mips, proc));
			}
			
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
			
			hosts = null;
			bandwidth = 0;
			processingCost = 0;			
		}		
		
		return new BrokerImpl(hosts, parseTasks(args[1]), bandwidth, processingCost, scheduler);
	}
}
