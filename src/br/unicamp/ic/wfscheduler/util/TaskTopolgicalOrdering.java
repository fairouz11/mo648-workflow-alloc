package br.unicamp.ic.wfscheduler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import br.unicamp.ic.wfscheduler.Task;

public class TaskTopolgicalOrdering
{
	private Hashtable<Task, ArrayList<Task>> nodes;
	private ArrayList<Task> sortedNodes;
	private boolean sorted;
	private Comparator<Task> taskComparator;

	/**
	 * Build topological sorter
	 * @param tasks to be sorted
	 */
	public TaskTopolgicalOrdering(List<Task> tasks)
	{
		this(tasks, null);
	}
	
	/**
	 * Build topological sorter
	 * @param tasks to be sorted
	 * @param taskComparator comparator to sort independent tasks
	 */
	public TaskTopolgicalOrdering(List<Task> tasks, Comparator<Task> taskComparator)
	{
		this.taskComparator = taskComparator;
		
		nodes = new Hashtable<Task, ArrayList<Task>>(tasks.size());
		sortedNodes = new ArrayList<Task> (tasks.size());
		sorted = false;		
		
		for (Task t : tasks)
			nodes.put(t, new ArrayList<Task>(t.getDependencies()));
	}
	
	/**
	 * Get tasks sorted by dependency, the nth task on the list can be safely executed before the mth task, for m > n.
	 * If taskComparator was informed, independent tasks are sorted according the comparator.
	 * @return array of sorted tasks
	 */
	public ArrayList<Task> getSortedTasks()
	{
		if (!sorted)
			sort();
		
		return new ArrayList<Task>(sortedNodes);
	}
	
	private void sort()
	{
		if (sorted)
			return;
		
		sorted = true;
		ArrayList<Task> removeNodes = new ArrayList<Task>();
		
		while (!nodes.isEmpty())
		{
			// search for "leafs" (nodes with out degree 0)
			for (Entry<Task, ArrayList<Task>> e : nodes.entrySet())
			{
				Task t = e.getKey();
				ArrayList<Task> dependencies = e.getValue();
				
				// if it's a leaf, then it has no dependency
				if (dependencies.isEmpty())
					removeNodes.add(t);
			}
			
			// sort leafs on same level
			if (taskComparator != null)
				Collections.sort(removeNodes, taskComparator);
			
			// remove leafs from node list
			for (Task t : removeNodes)
			{
				nodes.remove(t);
							
				// and add it to sortedNodes
				sortedNodes.add(t);
			}
					
			// remove leafs from edge list
			for (ArrayList<Task> dependencies : nodes.values())		
				dependencies.removeAll(removeNodes);
					
			removeNodes.clear();
		}
	}
}
