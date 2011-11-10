package br.unicamp.ic.wfscheduler.impl.roundrobin;

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

public class RoundRobinScheduler implements IScheduler
{
	private List<Task> tasks;
	private List<Host> hosts;
	
	@Override
	public void startScheduler(Broker broker)
	{		
		TaskTopolgicalOrdering tto;
		ArrayList<Task> orderedTasks;
		Hashtable<Task, Host> assignedTasks;
		ArrayList<Host> trans;
		int currentHost;
		
		tasks = broker.getTasks();
		hosts = broker.getHosts();
				
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
		currentHost = 0;
		
		// for each task
		while (orderedTasks.size() > 0)
		{
			Task t = orderedTasks.get(0);
			// round-robin host selected
			Host h = hosts.get(currentHost);
			currentHost = ( currentHost + 1 ) % hosts.size();			
			
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

	@Override
	public void transmissionFinished(Task task, Host sender, Host destionation)
	{
		// TODO Auto-generated method stub
		
	}

}
