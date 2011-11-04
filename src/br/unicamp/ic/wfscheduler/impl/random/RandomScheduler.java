package br.unicamp.ic.wfscheduler.impl.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.unicamp.ic.wfscheduler.Broker;
import br.unicamp.ic.wfscheduler.Host;
import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.Task;

public class RandomScheduler implements IScheduler
{
	private List<Task> tasks;
	private List<Host> hosts;
	
	@Override
	public void startScheduler(Broker broker)
	{		
		Random rnd;
		
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		
		rnd = new Random();
	
		/*
		while (tasks.size() > 0)
		{
			Task t = tasks.get(0);
			Host h = hosts.get(rnd.nextInt(hosts.size()));
			
			broker.assign(t, h);
			
			tasks.remove(0);
		}*/
		
		ArrayList<Host> trans = new ArrayList<Host>();
		
		broker.assign(tasks.get(0), hosts.get(0));
		broker.assign(tasks.get(1), hosts.get(0));
		broker.assign(tasks.get(2), hosts.get(2));
		
		try
		{
			trans.add(hosts.get(2));
			//trans.add(hosts.get(2));
			broker.transmitResult(tasks.get(0), trans);
			
			trans.clear();
			trans.add(hosts.get(2));
			broker.transmitResult(tasks.get(1), trans);
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void taskFinished(Task t, Host h)
	{
		return;
	}

}
