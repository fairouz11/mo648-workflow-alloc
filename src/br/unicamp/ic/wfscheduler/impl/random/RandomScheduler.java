package br.unicamp.ic.wfscheduler.impl.random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import br.unicamp.ic.wfscheduler.Broker;
import br.unicamp.ic.wfscheduler.Host;
import br.unicamp.ic.wfscheduler.IScheduler;
import br.unicamp.ic.wfscheduler.Task;
import br.unicamp.ic.wfscheduler.util.TaskTopolgicalOrdering;

public class RandomScheduler implements IScheduler
{
	private List<Task> tasks;
	private List<Host> hosts;
	
	@Override
	public void startScheduler(Broker broker)
	{		
		Random rnd;
		TaskTopolgicalOrdering tto;
		ArrayList<Task> orderedTasks;
		Hashtable<Task, Host> assignedTasks;
		ArrayList<Host> trans;
		
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		
		rnd = new Random();
		tto = new TaskTopolgicalOrdering(tasks, new Comparator<Task>()
		{
			@Override
			public int compare(Task a, Task b)
			{
				if (a.getLength() < b.getLength())
					return -1;
				else if (a.getLength() > b.getLength())
					return 1;
				return 0;
			}
		});
		
		orderedTasks = tto.getSortedTasks();
		assignedTasks = new Hashtable<Task, Host>(orderedTasks.size());
		trans = new ArrayList<Host>();
		
		// for each task
		while (orderedTasks.size() > 0)
		{
			Task t = orderedTasks.get(0);
			// choose a random host to assign it to the task
			Host h = hosts.get(rnd.nextInt(hosts.size()));
			
			broker.assign(t, h); 
			assignedTasks.put(t, h);
							
			// now, we have to transmit the dependencies to this host
			// as we have topologically ordered the tasks, all dependencies
			// should be assigned by now
			trans.add(h);
			
			for (Task dep : t.getDependencies())
				broker.transmitResult(dep, trans);

			trans.clear();
			
			orderedTasks.remove(0);
		}
		
		return;
	}
	
	public void taskFinished(Task t, Host h)
	{
		return;
	}

}
