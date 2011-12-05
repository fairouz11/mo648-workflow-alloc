package randomDAG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.edge.DirectedWeightedEdge;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

import randomDAG.Task;



public class randomDAG {
	public static void main(String[] args) {
		Random generator = new Random();
		DirectedGraph g = new DefaultDirectedGraph();
		boolean verbose;
		int maxSize, minSize, minTaskCost, maxTaskCost, minCommCost, maxCommCost, DAGNumber;
		
		DAGNumber=Integer.valueOf(args[0]).intValue();
		minSize=Integer.valueOf(args[1]).intValue();
		maxSize=Integer.valueOf(args[2]).intValue();
		minTaskCost=Integer.valueOf(args[3]).intValue();
		maxTaskCost=Integer.valueOf(args[4]).intValue();
		minCommCost=Integer.valueOf(args[5]).intValue();
		maxCommCost=Integer.valueOf(args[6]).intValue();
		
		
		if(args[7].compareToIgnoreCase("verbose") == 0)
			verbose=true;
		else 
			verbose=false;
		
		
		
		switch (DAGNumber) {
		case 1: // Random DAG
			System.out.println("Creating Random DAG (minSize=" + minSize+ ", maxSize=" + maxSize + ", minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createRandomDAG(generator.nextInt(maxSize-minSize) + minSize, minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 2: 
			System.out.println("Creating AIRSN DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createAIRSNGraph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 3:
			System.out.println("Creating Chimera1 DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createChimera1Graph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 4:
			System.out.println("Creating Chimera2 DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createChimera2Graph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 5:
			System.out.println("Creating CSTEM DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createCSTEMGraph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 6:
			System.out.println("Creating PaperLIGO DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createAIRSNGraph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 7:
			System.out.println("Creating SlidesLIGO DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createSlidesLIGOGraph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 8:
			System.out.println("Creating PaperMontage DAG (minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost+")");
			g = createPaperMontageGraph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		case 9:
			Random randomgenerator = new Random();
			int randQtdeNosCode = randomgenerator.nextInt(19); 
			int randQtdeNos=0;
			switch (randQtdeNosCode) {
			case 0: randQtdeNos=101; break;
			case 1: randQtdeNos=11; break;
			case 2: randQtdeNos=15; break;
			case 3: randQtdeNos=21; break;
			case 4: randQtdeNos=25; break;
			case 5: randQtdeNos=31; break;
			case 6: randQtdeNos=35; break;
			case 7: randQtdeNos=41; break;
			case 8: randQtdeNos=45; break;
			case 9: randQtdeNos=51; break;
			case 10: randQtdeNos=55; break;
			case 11: randQtdeNos=61; break;
			case 12: randQtdeNos=65; break;
			case 13: randQtdeNos=71; break;
			case 14: randQtdeNos=75; break;
			case 15: randQtdeNos=81; break;
			case 16: randQtdeNos=85; break;
			case 17: randQtdeNos=91; break;
			case 18: randQtdeNos=95; break;
			}
			System.out.println("Creating SiteMontage DAG (size="+ randQtdeNos+ ", minTaskCost=" + minTaskCost + ", maxTaskCost=" + maxTaskCost + ", minCommCost=" + minCommCost + ", maxCommCost=" + maxCommCost);
			g = createSiteMontageGraph(minTaskCost, maxTaskCost, minCommCost, maxCommCost, verbose, randQtdeNos);
			System.out.println(g.vertexSet().size());
			printNodesList(g);
			printEdgesList(g);
			break;
		}
		
	}
	
	
	public static int getTaskWeight(int min, int max) {
		Random generator = new Random();

		return generator.nextInt(max-min) + min;
	}

	public static int getEdgeWeight(int min, int max) {
			Random generator = new Random();
			return generator.nextInt(max-min) + min;
	}
	
	public static void printEdgesList(DirectedGraph g) {
		Iterator<?> itEdges = g.edgeSet().iterator();
		
		
		while(itEdges.hasNext()) {
			DirectedWeightedEdge e = (DirectedWeightedEdge) itEdges.next();
			Task source=(Task) e.getSource();
			Task target=(Task) e.getTarget();
			System.out.println(source.number +" -> "+ target.number + " : " + e.getWeight());
		}
	}
		
	public static void printNodesList(DirectedGraph g) {
		Iterator<?> itNodes  = g.vertexSet().iterator();
		
		while(itNodes.hasNext()) {
			Task t = (Task) itNodes.next();
			System.out.println(t.number +" : "+ t.lines);
		}
	}
	
	public static DirectedGraph createRandomDAG(int size, int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph();
		Random generator = new Random();
		int edgeCount=0;
		int taskID;
		int currentLevel;
		double probabilityEdgeExists=1;
		
		
		//ArrayList "tasks" stores ArrayLists of tasks per level 
		//Arraylist position 0 stores tasks from level 0
		ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>(); 
		ArrayList<Task> taskEntry = new ArrayList<Task>();
		ArrayList<Task> tarefaExit = new ArrayList<Task>();

		if(verbose)
			System.out.println("RandomDAG size=" + size);
		
		//create entry and exit nodes
		taskID=1;
		Task t1 = new Task(taskID, getTaskWeight(minTaskCost, maxTaskCost));
		taskID=size;
		Task tN = new Task(taskID, getTaskWeight(minTaskCost, maxTaskCost));
		t1.maxLevel=0;
		g.addVertex(t1);
		g.addVertex(tN);
		 
		taskEntry.add(t1);
		tarefaExit.add(tN);
		
		
		//Stores task set "taskEntry" in position 0 
		tasks.add(taskEntry);
		
		
		taskID=2;
		currentLevel=1;
		
		//while size(G) < N do
		while(taskID < size) {
			int factor=generator.nextInt(2)+1;
			int limit=(size - g.vertexSet().size())/factor;
			int k;
			if(limit > 1)
				k=generator.nextInt(limit)+1;
			else
				k=1;
			
			//V_nivel = k new nodes
			int qtdeTasksNextLevel = k; //generator.nextInt( (int) (0.5 + (size-taskID)/2.0)) + 1;
			
			ArrayList<Task> tasksCurrentLevel=new ArrayList<Task>();

			if(verbose) 
				System.out.println("RandomDAG:Level " + currentLevel + ", level size = " + qtdeTasksNextLevel);
			for(int i=0; i<qtdeTasksNextLevel; i++) {
				Task t = new Task(taskID,  getTaskWeight(minTaskCost, maxTaskCost));
				g.addVertex(t);
				tasksCurrentLevel.add(t);
				if(verbose)
					System.out.println("RandomDAG: Added task " + t.number);
				t.maxLevel=currentLevel;
				taskID++;	
			}
			
			
			Iterator<Task> itTarefasCurrentLevel = tasksCurrentLevel.iterator();
			while(itTarefasCurrentLevel.hasNext()) {
				Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
				ArrayList<?> tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
				Iterator<?> itTarefasParentLevel = tasksParentLevel.iterator();
				
				while(itTarefasParentLevel.hasNext()) {
					Task tParentLevel = (Task) itTarefasParentLevel.next();
					Random rand = new Random();
					double teste=rand.nextDouble();
					//System.out.println("random=" +teste + "<" + probabilityEdgeExists);
					if(teste <= probabilityEdgeExists) {
						DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
						g.addEdge(edge);
						if(verbose)
							System.out.println("   RandomDAG: Added edge: " + tParentLevel.number + ":" + tCurrentLevel.number);
						edgeCount++;
						
					}
				}
				
			}
			
			tasks.add(tasksCurrentLevel);

			if (tasksCurrentLevel.size() == 1) {
				probabilityEdgeExists=1.0;
			}
			else {
				probabilityEdgeExists=1.0/2.0;
			}
/*			if (tasksCurrentLevel.size() == 2) {
				probabilityEdgeExists=1.0/2.0;
			}
			else {
				probabilityEdgeExists=2.0/tasksCurrentLevel.size();
			}*/
			
			currentLevel++;
			
		}
		
		tN.maxLevel=currentLevel;
		
		//Create edges between tasks from the last level to the exit node  
		ArrayList<?> tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		Iterator<?> itTarefasParentLevel = tasksParentLevel.iterator();
		while(itTarefasParentLevel.hasNext()) {
			Task tParentLevel = (Task) itTarefasParentLevel.next();
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tN , getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			if(verbose)
				System.out.println("   RandomDAG: Add last level edge: " + tParentLevel.number + ":" + tN.number);			
			edgeCount++;
		}

		tasks.add(tarefaExit);
		
		
		
		//add arcs to tasks which have no source or target arcs inciding to it
		Iterator<ArrayList<Task>> itTarefas = tasks.iterator();
		int level=1;
		itTarefas.next();
		while(itTarefas.hasNext()) {
			ArrayList<?> tasksCurrentLevel = (ArrayList<?>) itTarefas.next();
			Iterator<?> itTarefasCurrentLevel = tasksCurrentLevel.iterator();
			while(itTarefasCurrentLevel.hasNext()) {
				Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
				if(g.inDegreeOf(tCurrentLevel) == 0) {
					tasksParentLevel = (ArrayList<?>) tasks.get(level-1);
					Random rand = new Random();
					Task tParent = (Task) tasksParentLevel.get(rand.nextInt(tasksParentLevel.size()));
					DirectedWeightedEdge edge = new DirectedWeightedEdge(tParent, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
					g.addEdge(edge);
					if(verbose)
						System.out.println("   RandomDAG:Edge-1: " + tParent.number + ":" + tCurrentLevel.number);
					edgeCount++;
				}

				if((g.outDegreeOf(tCurrentLevel) == 0) && (tCurrentLevel.number != tN.number)) {
					ArrayList<?> tasksChildLevel = (ArrayList<?>) tasks.get(level+1);
					Random rand = new Random();
					Task tChild = (Task) tasksChildLevel.get(rand.nextInt(tasksChildLevel.size()));
					DirectedWeightedEdge edge = new DirectedWeightedEdge(tCurrentLevel, tChild, getEdgeWeight(minCommCost, maxCommCost));
					g.addEdge(edge);
					if(verbose)
						System.out.println("   RandomDAG:Edge-2: " + tCurrentLevel.number + ":" + tChild.number);
					edgeCount++;
				}
				
				//Add arcs among different levels 
				if((tCurrentLevel.number != tN.number) && (tCurrentLevel.maxLevel < (tN.maxLevel-1))) {
					int randomLevel = generator.nextInt(tN.maxLevel - tCurrentLevel.maxLevel-1) + tCurrentLevel.maxLevel + 2;
					ArrayList<?> tasksRandomLevel = (ArrayList<?>) tasks.get(randomLevel);
					Task randomTask = (Task) tasksRandomLevel.get(generator.nextInt(tasksRandomLevel.size()));
					int createEdge=generator.nextInt(10);
					if(createEdge==1) {
						DirectedWeightedEdge edge = new DirectedWeightedEdge(tCurrentLevel, randomTask, getEdgeWeight(minCommCost, maxCommCost));
						g.addEdge(edge);
						if(verbose)
							System.out.println("   RandomDAG: Added multilevel edge: " + tCurrentLevel.number + "(" + tCurrentLevel.maxLevel + ") : " + randomTask.number +"(" + randomTask.maxLevel + ")");
						edgeCount++;

					}
				}
				
				
			}
			level++;
		}
		
		if(verbose)
			System.out.println("END - RANDOM DAG GENERATION - edgeCount=" + edgeCount);
		
		return g;
				
	}
	
	public static DirectedGraph createSiteMontageGraph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose, int qtdeTasks) {
		DirectedGraph g = new DefaultDirectedGraph(  );

		Task t1 = new Task(1, getTaskWeight(minTaskCost, maxTaskCost));
		Task tN = new Task(qtdeTasks+2,  0);
		
		g.addVertex(t1);
		g.addVertex(tN);
		
		ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
		ArrayList<Task> taskEntry = new ArrayList<Task>();
		ArrayList<Task> tarefaExit = new ArrayList<Task>();
		
		taskEntry.add(t1);
		tarefaExit.add(tN);
		
		tasks.add(taskEntry);
		

		int numTask=2;
		int currentLevel=2;

		if(verbose)
			System.out.println("MontageSite: qtdeTasks=" + qtdeTasks);

		int qtdeTasksNextLevel = (qtdeTasks -1)/5;
			
		if(qtdeTasks % 2 != 0) 
			qtdeTasksNextLevel = qtdeTasks/5;
		
		int largura=qtdeTasksNextLevel;
		ArrayList<Task> tasksCurrentLevel=new ArrayList<Task>();

		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", number of tasks = " + qtdeTasksNextLevel);
		
		for(int i=0; i<qtdeTasksNextLevel; i++) {
			Task t = new Task(numTask,  getTaskWeight(minTaskCost, maxTaskCost));

			g.addVertex(t);
			tasksCurrentLevel.add(t);

			if(verbose) 
				System.out.println("MontageSite: Add task " + t.number);
			numTask++;
		}
		Iterator<Task> itTarefasCurrentLevel = tasksCurrentLevel.iterator();
		while(itTarefasCurrentLevel.hasNext()) {
			Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
			DirectedWeightedEdge edge = new DirectedWeightedEdge(t1, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			if(verbose)
				System.out.println("   MontageSite:Edge: " + t1.number + ":" + tCurrentLevel.number);

		}
		
		tasks.add(tasksCurrentLevel);
		currentLevel++;
		
		
		
		
		tasksCurrentLevel=new ArrayList<Task>();

		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", number of tasks = " + qtdeTasksNextLevel);
		
		for(int i=0; i<qtdeTasksNextLevel; i++) {
			Task t = new Task(numTask, getTaskWeight(minTaskCost, maxTaskCost));

			g.addVertex(t);
			tasksCurrentLevel.add(t);

			if(verbose)
				System.out.println("MontageSite: Added task " + t.number);
			numTask++;
		}
		
		itTarefasCurrentLevel = tasksCurrentLevel.iterator();
		ArrayList<?> tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		Iterator<?> itTarefasParentLevel = tasksParentLevel.iterator();
		while(itTarefasCurrentLevel.hasNext()) {
			Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
			Task tParentLevel = (Task) itTarefasParentLevel.next();

			DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			if(verbose)
				System.out.println("   MontageSite:Edge: " + tParentLevel.number + ":" + tCurrentLevel.number);

		}
		
		tasks.add(tasksCurrentLevel);
		currentLevel++;
		
		tasksCurrentLevel=new ArrayList<Task>();

		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", qtde tasks = " + qtdeTasksNextLevel);
		
		for(int i=0; i<qtdeTasksNextLevel; i++) {
			Task t = new Task(numTask, getTaskWeight(minTaskCost, maxTaskCost));

			g.addVertex(t);
			tasksCurrentLevel.add(t);

			if(verbose)
				System.out.println("MontageSite: Added task " + t.number);
			numTask++;
		}
		
		itTarefasCurrentLevel = tasksCurrentLevel.iterator();
		tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		itTarefasParentLevel = tasksParentLevel.iterator();
		while(itTarefasCurrentLevel.hasNext()) {
			Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
			Task tParentLevel = (Task) itTarefasParentLevel.next();

			DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			if(verbose)
				System.out.println("   MontageSite:Edge: " + tParentLevel.number + ":" + tCurrentLevel.number);

		}
		
		tasks.add(tasksCurrentLevel);
		currentLevel++;
		
		tasksCurrentLevel=new ArrayList<Task>();
		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", qtde tasks = " + qtdeTasksNextLevel);
		Task t = new Task(numTask, getTaskWeight(minTaskCost, maxTaskCost));
		g.addVertex(t);
		tasksCurrentLevel.add(t);
		
		if(verbose)
			System.out.println("MontageSite: Added task " + t.number);
		numTask++;
		
		tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		itTarefasParentLevel = tasksParentLevel.iterator();
		while(itTarefasParentLevel.hasNext()) {
			Task tParentLevel = (Task) itTarefasParentLevel.next();
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, t, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			if(verbose)
				System.out.println("   MontageSite:Edge: " + tParentLevel.number + ":" + t.number);

		}
		tasks.add(tasksCurrentLevel);
		
		
		tasksCurrentLevel=new ArrayList<Task>();

		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", qtde tasks = " + qtdeTasksNextLevel);
		
		for(int i=0; i<qtdeTasksNextLevel; i++) {
			t = new Task(numTask, getTaskWeight(minTaskCost, maxTaskCost));
			
			g.addVertex(t);
			tasksCurrentLevel.add(t);

			if(verbose)
				System.out.println("MontageSite: Added task " + t.number);
			numTask++;
		}
		
		tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		Task tParentLevel = (Task) tasksParentLevel.get(0);

		itTarefasCurrentLevel = tasksCurrentLevel.iterator();
		while(itTarefasCurrentLevel.hasNext()) {
			Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
		
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			if(verbose)
				System.out.println("   MontageSite:Edge: " + tParentLevel.number + ":" + tCurrentLevel.number);

		}
		tasks.add(tasksCurrentLevel);
		currentLevel++;
		
		tasksCurrentLevel=new ArrayList<Task>();
		qtdeTasksNextLevel=qtdeTasksNextLevel/2;
		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", qtde tasks = " + qtdeTasksNextLevel);
		
		for(int i=0; i<qtdeTasksNextLevel; i++) {
			t = new Task(numTask, getTaskWeight(minTaskCost, maxTaskCost));

			g.addVertex(t);
			tasksCurrentLevel.add(t);

			if(verbose)
				System.out.println("MontageSite: Added task " + t.number);
			numTask++;
		}
		
		int qtdeParents=2;
		if(largura % 2 !=0 ){
			qtdeParents=3;
		}
		itTarefasCurrentLevel = tasksCurrentLevel.iterator();
		tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		itTarefasParentLevel = tasksParentLevel.iterator();
		while(itTarefasCurrentLevel.hasNext()) {
			Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
			for(int i=0; i<qtdeParents; i++) {
				tParentLevel = (Task) itTarefasParentLevel.next();

				DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
				if(verbose)
					System.out.println("   MontageSite:Edge: " + tParentLevel.number + ":" + tCurrentLevel.number);
			}
			qtdeParents=2;
	   }
		
		tasks.add(tasksCurrentLevel);
		currentLevel++;
		

		
		tasksCurrentLevel=new ArrayList<Task>();
		
		if(verbose)
			System.out.println	("MontageSite:Level " + currentLevel + ", qtde tasks = " + qtdeTasksNextLevel);
		
		for(int i=0; i<qtdeTasksNextLevel; i++) {
			t = new Task(numTask, getTaskWeight(minTaskCost, maxTaskCost));

			g.addVertex(t);
			tasksCurrentLevel.add(t);

			if(verbose)
				System.out.println("MontageSite: Added task " + t.number);
			numTask++;
		}
		
		itTarefasCurrentLevel = tasksCurrentLevel.iterator();
		tasksParentLevel = (ArrayList<?>) tasks.get(tasks.size()-1);
		itTarefasParentLevel = tasksParentLevel.iterator();
		while(itTarefasCurrentLevel.hasNext()) {
			Task tCurrentLevel = (Task) itTarefasCurrentLevel.next();
			tParentLevel = (Task) itTarefasParentLevel.next();
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tParentLevel, tCurrentLevel, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			
			if(verbose)
				System.out.println("   MontageSite:Edge: " + tParentLevel.number + ":" + tCurrentLevel.number);
			
			edge = new DirectedWeightedEdge(tCurrentLevel, tN, 0);
			g.addEdge(edge);
			if(verbose)
				System.out.println("   MontageSite:Edge: " + tCurrentLevel.number + ":" + tN.number);
		}
		
		tasks.add(tasksCurrentLevel);
		currentLevel++;
		
		
		tasks.add(tarefaExit);
		

		return g;
		
		
	}
	public static DirectedGraph createPaperMontageGraph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

		
		
		Task t1 = new Task(1,  0);
		
		Task t2 = new Task(2,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t3 = new Task(3,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t4 = new Task(4,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t5 = new Task(5,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t6 = new Task(6,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t7 = new Task(7,  getTaskWeight(minTaskCost, maxTaskCost));
		
		Task t8 = new Task(8, getTaskWeight(minTaskCost, maxTaskCost));
		Task t9 = new Task(9, getTaskWeight(minTaskCost, maxTaskCost));
		Task t10 = new Task(10, getTaskWeight(minTaskCost, maxTaskCost));
		Task t11 = new Task(11, getTaskWeight(minTaskCost, maxTaskCost));
		Task t12 = new Task(12, getTaskWeight(minTaskCost, maxTaskCost));
		Task t13 = new Task(13, getTaskWeight(minTaskCost, maxTaskCost));
		Task t14 = new Task(14, getTaskWeight(minTaskCost, maxTaskCost));
		Task t15 = new Task(15, getTaskWeight(minTaskCost, maxTaskCost));

		
		
		Task t16 = new Task(16 , getTaskWeight(minTaskCost, maxTaskCost));
		
		Task t17 = new Task(17,  getTaskWeight(minTaskCost, maxTaskCost));
		
		Task t18 = new Task(18,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t19 = new Task(19,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t20 = new Task(20,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t21 = new Task(21,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t22 = new Task(22,  getTaskWeight(minTaskCost, maxTaskCost));
		Task t23 = new Task(23,  getTaskWeight(minTaskCost, maxTaskCost));
		
		Task t24 = new Task(24,  getTaskWeight(minTaskCost, maxTaskCost));
		
		Task t25 = new Task(25,  getTaskWeight(minTaskCost, maxTaskCost)); 
		
			
		g.addVertex(t1);
		g.addVertex(t2);
		g.addVertex(t3);
		g.addVertex(t4);
		g.addVertex(t5);
		g.addVertex(t6);
		g.addVertex(t7);
		g.addVertex(t8);
		g.addVertex(t9);
		g.addVertex(t10);
		g.addVertex(t11);
		g.addVertex(t12);
		g.addVertex(t13);
		g.addVertex(t14);
		g.addVertex(t15);
		g.addVertex(t16);
		g.addVertex(t17);
		g.addVertex(t18);
		g.addVertex(t19);
		g.addVertex(t20);
		g.addVertex(t21);
		g.addVertex(t22);
		g.addVertex(t23);
		g.addVertex(t24);
		g.addVertex(t25);
			
		
		
		DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t2, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t1,t3, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t1,t4, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t1,t5, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t1,t6, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t1,t7, 0);
		g.addEdge(edge);
	
		edge = new DirectedWeightedEdge(t2,t8, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t2,t9, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t2,t18, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t3,t8, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t3,t10, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t3,t19, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t9, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t10, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t11, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t12, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t21, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t5,t11, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t5,t13, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t5,t14, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t5,t20, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t6,t12, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t6,t13, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t6,t15, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t6,t22, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t7,t14, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t7,t15, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t7,t23, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		
		edge = new DirectedWeightedEdge(t8,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t9,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t10,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t11,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t12,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t13,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t14,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t15,t16, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		edge = new DirectedWeightedEdge(t16,t17, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		edge = new DirectedWeightedEdge(t17,t18, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t17,t19, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t17,t20, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t17,t21, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t17,t22, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t17,t23, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		
		edge = new DirectedWeightedEdge(t18,t24, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t19,t24, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t20,t24, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t21,t24, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t22,t24, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t23,t24, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		edge = new DirectedWeightedEdge(t18,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t19,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t20,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t21,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t22,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t23,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		edge = new DirectedWeightedEdge(t24,t25, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		
		return g;
		
	}
	
	public static DirectedGraph createCSTEMGraph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

		int weight=getTaskWeight(minTaskCost, maxTaskCost);
	
		
		Task t1 = new Task(1,  (int) weight);
		Task t2 = new Task(2,  (int) (0.6/4.8*weight));
		Task t3 = new Task(3,  (int) (0.3/4.8*weight));
		Task t4 = new Task(4,  (int) (0.6/4.8*weight));
		Task t5 = new Task(5,  (int) (23/4.8*weight));
		Task t6 = new Task(6,  (int) (23/4.8*weight));
		Task t7 = new Task(7,  (int) (1.4/4.8*weight));
		Task t8 = new Task(8,  (int) (2.0/4.8*weight));
		Task t9 = new Task(9,  (int) (0.5/4.8*weight));
		Task t10 = new Task(10,  (int) (0.4/4.8*weight));
		Task t11 = new Task(11,  (int) (6.5/4.8*weight));
		Task t12 = new Task(12,  (int) (0.9/4.8*weight));
		Task t13 = new Task(13,  (int) (90/4.8*weight));
		Task t14 = new Task(14,  (int) (6.5/4.8*weight));
		Task t15 = new Task(15,  (int) (5.3/4.8*weight));
		Task t16 = new Task(16,  0);

		
		g.addVertex(t1);
		g.addVertex(t2);
		g.addVertex(t3);
		g.addVertex(t4);
		g.addVertex(t5);
		g.addVertex(t6);
		g.addVertex(t7);
		g.addVertex(t8);
		g.addVertex(t9);
		g.addVertex(t10);
		g.addVertex(t11);
		g.addVertex(t12);
		g.addVertex(t13);
		g.addVertex(t14);
		g.addVertex(t15);
		g.addVertex(t16);


		
	
		DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t2, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t1,t3, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t2,t4, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t3,t4, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t5, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t6, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t4,t7, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t5,t16, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t6,t8, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t6,t9, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t7,t16, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t8,t10, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t9,t10, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t9,t11, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t10,t12, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t10,t13, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t11,t14, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t12,t15, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t12,t14, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t13,t15, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t14,t16, 0);
		g.addEdge(edge);
		edge = new DirectedWeightedEdge(t15,t16, 0);
		g.addEdge(edge);

		
		return g;
	}

	public static DirectedGraph createPaperLIGOGraph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

			
		ArrayList<Task> tasks=new ArrayList<Task>();
		
		Task t1 = new Task(1,  0);
		g.addVertex(t1);
		
		tasks.add(t1);
		
		for(int i=2; i<=167; i++) {
			Task t = new Task(i,  getTaskWeight(minTaskCost, maxTaskCost));
			g.addVertex(t);
			tasks.add(t);
		}
		
		
		for(int i=2; i<=35; i++) {
			Task t = (Task) tasks.get(i-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t, 0);
			g.addEdge(edge);
			 
		}
		
		
		for(int i=2; i<=35; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(i-1+34);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 			
		}
		
		
		for(int i=35; i<44; i++) {
			Task tSource = (Task) tasks.get(i);
			Task tTarget = (Task) tasks.get(70-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

		
		for(int i=45; i<=57; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(71-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		
		for(int i=57; i<=63; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(72-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=63; i<=65; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(73-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

		for(int i=61; i<=65; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(74-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=63; i<=67; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(75-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

		for(int i=66; i<=69; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(76-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=77; i<=85; i++) {
			Task tSource = (Task) tasks.get(70-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		
		for(int i=86; i<=98; i++) {
			Task tSource = (Task) tasks.get(71-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=98; i<=104; i++) {
			Task tSource = (Task) tasks.get(72-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=105; i<=107; i++) {
			Task tSource = (Task) tasks.get(73-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=108; i<=112; i++) {
			Task tSource = (Task) tasks.get(74-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		
		for(int i=110; i<=114; i++) {
			Task tSource = (Task) tasks.get(75-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=115; i<=118; i++) {
			Task tSource = (Task) tasks.get(76-1);
			Task tTarget = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
	
		for(int i=77; i<=118; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(i-1+42);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=119; i<=127; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(161-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		

		for(int i=128; i<=140; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(162-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		

		for(int i=140; i<=146; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(163-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		

		for(int i=147; i<=149; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(164-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		

		for(int i=150; i<=154; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(165-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		

		for(int i=152; i<=156; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(166-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

		for(int i=157; i<=160; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(167-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		Task t168 = new Task(168,  0);
		g.addVertex(t168);

		for(int i=161; i<=167; i++) {
			Task tSource = (Task) tasks.get(i-1);
			
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,t168, 0);
			g.addEdge(edge);
			System.out.println(tSource.number + "->" + t168.number);
		}
	
		
		return g;
		
	}
	
	
	
	public static DirectedGraph createSlidesLIGOGraph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

			
		ArrayList<Task> tasks=new ArrayList<Task>();
		
		Task t1 = new Task(1,  0);
		g.addVertex(t1);
		
		tasks.add(t1);
		
		for(int i=2; i<=76; i++) {
			Task t = new Task(i,  getTaskWeight(minTaskCost, maxTaskCost));
			g.addVertex(t);
			tasks.add(t);
		}
		
		
		for(int i=2; i<=8; i++) {
			Task t = (Task) tasks.get(i-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
			 
		}
		
	
		for(int i=2; i<=7; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(i-1+7);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=15; i<=18; i++) {
			Task tSource = (Task) tasks.get(8-1);
			Task tTarget = (Task) tasks.get(i-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		Task tSource = (Task) tasks.get(9-1);
		Task tTarget = (Task) tasks.get(19-1);
		DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(10-1);
		tTarget = (Task) tasks.get(19-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(20-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(21-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(60-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		
		tSource = (Task) tasks.get(11-1);
		tTarget = (Task) tasks.get(19-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(20-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(21-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(22-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tTarget = (Task) tasks.get(60-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(12-1);
		tTarget = (Task) tasks.get(20-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(21-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(22-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(23-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tTarget = (Task) tasks.get(42-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(13-1);
		tTarget = (Task) tasks.get(22-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(23-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(25-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tTarget = (Task) tasks.get(42-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(43-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(14-1);
		tTarget = (Task) tasks.get(22-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(23-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(25-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tTarget = (Task) tasks.get(43-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(15-1);
		tTarget = (Task) tasks.get(24-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(26-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(27-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		
		
		tSource = (Task) tasks.get(16-1);
		tTarget = (Task) tasks.get(28-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(17-1);
		tTarget = (Task) tasks.get(29-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(18-1);
		tTarget = (Task) tasks.get(30-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(19-1);
		tTarget = (Task) tasks.get(54-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(20-1);
		tTarget = (Task) tasks.get(55-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		
		tSource = (Task) tasks.get(21-1);
		tTarget = (Task) tasks.get(31-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(22-1);
		tTarget = (Task) tasks.get(32-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(23-1);
		tTarget = (Task) tasks.get(33-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(24-1);
		tTarget = (Task) tasks.get(31-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(32-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(33-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(34-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(37-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(25-1);
		tTarget = (Task) tasks.get(34-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(26-1);
		tTarget = (Task) tasks.get(35-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(27-1);
		tTarget = (Task) tasks.get(36-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(28-1);
		tTarget = (Task) tasks.get(38-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(39-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(53-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(29-1);
		tTarget = (Task) tasks.get(39-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(40-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(41-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(70-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	

		tSource = (Task) tasks.get(30-1);
		tTarget = (Task) tasks.get(41-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(31-1);
		tTarget = (Task) tasks.get(42-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(32-1);
		tTarget = (Task) tasks.get(42-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(33-1);
		tTarget = (Task) tasks.get(43-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(34-1);
		tTarget = (Task) tasks.get(43-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(35-1);
		tTarget = (Task) tasks.get(44-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(46-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(59-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		tSource = (Task) tasks.get(36-1);
		tTarget = (Task) tasks.get(44-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(46-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(69-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(37-1);
		tTarget = (Task) tasks.get(45-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(47-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(48-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		

		tSource = (Task) tasks.get(38-1);
		tTarget = (Task) tasks.get(47-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		
		tSource = (Task) tasks.get(39-1);
		tTarget = (Task) tasks.get(48-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	

		tSource = (Task) tasks.get(40-1);
		tTarget = (Task) tasks.get(63-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	

		tSource = (Task) tasks.get(41-1);
		tTarget = (Task) tasks.get(64-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		

		tSource = (Task) tasks.get(42-1);
		tTarget = (Task) tasks.get(49-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(56-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(57-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(71-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(74-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		
		Task t77 = new Task(77,  0);
		g.addVertex(t77);
		tasks.add(t77);
		
		
		tSource = (Task) tasks.get(43-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, 0);
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(44-1);
		tTarget = (Task) tasks.get(51-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		
		tSource = (Task) tasks.get(45-1);
		tTarget = (Task) tasks.get(50-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(51-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(52-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(46-1);
		tTarget = (Task) tasks.get(52-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		
		tSource = (Task) tasks.get(47-1);
		tTarget = (Task) tasks.get(53-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(48-1);
		tTarget = (Task) tasks.get(53-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(49-1);
		tTarget = (Task) tasks.get(66-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		
		tSource = (Task) tasks.get(50-1);
		tTarget = (Task) tasks.get(54-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(55-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(58-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 
		tSource = (Task) tasks.get(51-1);
		tTarget = (Task) tasks.get(59-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(52-1);
		tTarget = (Task) tasks.get(59-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(53-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, 0);
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(54-1);
		tTarget = (Task) tasks.get(60-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(55-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, 0);
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(56-1);
		tTarget = (Task) tasks.get(65-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);

		tSource = (Task) tasks.get(57-1);
		tTarget = (Task) tasks.get(67-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);

		tSource = (Task) tasks.get(58-1);
		tTarget = (Task) tasks.get(61-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);

		tSource = (Task) tasks.get(59-1);
		tTarget = (Task) tasks.get(62-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(69-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(75-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(76-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);

		tSource = (Task) tasks.get(60-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, 0);
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(61-1);
		tTarget = (Task) tasks.get(65-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(66-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(67-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(68-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(62-1);
		tTarget = (Task) tasks.get(72-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		tSource = (Task) tasks.get(63-1);
		tTarget = (Task) tasks.get(70-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		tSource = (Task) tasks.get(64-1);
		tTarget = (Task) tasks.get(70-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		
		tSource = (Task) tasks.get(65-1);
		tTarget = (Task) tasks.get(71-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		tSource = (Task) tasks.get(66-1);
		tTarget = (Task) tasks.get(71-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(67-1);
		tTarget = (Task) tasks.get(71-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(68-1);
		tTarget = (Task) tasks.get(72-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(73-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		tSource = (Task) tasks.get(69-1);
		tTarget = (Task) tasks.get(73-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		tSource = (Task) tasks.get(70-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, 0);
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(71-1);
		tTarget = (Task) tasks.get(74-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(72-1);
		tTarget = (Task) tasks.get(75-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(73-1);
		tTarget = (Task) tasks.get(75-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(74-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, 0);
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(75-1);
		tTarget = (Task) tasks.get(76-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(76-1);
		tTarget = (Task) tasks.get(77-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		
		return g;
	}
	
	public static DirectedGraph createAIRSNGraph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

			
		ArrayList<Task> tasks=new ArrayList<Task>();
		
		// Add costless entry node
		Task t1 = new Task(1, 0);
		g.addVertex(t1);
		tasks.add(t1);
		
		for(int i=2; i<=52; i++) {
			Task t = new Task(i, getTaskWeight(minTaskCost, maxTaskCost));
			g.addVertex(t);
			tasks.add(t);
		}
		
		
		for(int i=2; i<=11; i++) {
			Task t = (Task) tasks.get(i-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t, 0);
			g.addEdge(edge);
		}
		
		for(int i=2; i<=11; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(i-1+10);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
		}
		
		for(int i=2; i<=11; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(i-1+29);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
		}
	
		Task tSource = (Task) tasks.get(6-1);
		Task tTarget = (Task) tasks.get(22-1);
		DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(23-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(24-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
	
		
		
		// Add costless exit node
		Task t53 = new Task(53,  0);
		g.addVertex(t53);
		tasks.add(t53);
	
	
		for(int i=12; i<=14; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(53-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, 0);
			g.addEdge(edge);
		}
		

		tSource = (Task) tasks.get(15-1);
		tTarget = (Task) tasks.get(22-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		tTarget = (Task) tasks.get(25-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
		
		tSource = (Task) tasks.get(16-1);
		tTarget = (Task) tasks.get(23-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(26-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(17-1);
		tTarget = (Task) tasks.get(24-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		tTarget = (Task) tasks.get(27-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		for(int i=18; i<=21; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(53-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, 0);
			g.addEdge(edge);
	 		}
	
		for(int i=22; i<=24; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(i-1+3);
			edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
	
		
		for(int i=25; i<=27; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(28-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
	
		tSource = (Task) tasks.get(28-1);
		tTarget = (Task) tasks.get(29-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		tSource = (Task) tasks.get(29-1);
		tTarget = (Task) tasks.get(30-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 		
		
		for(int i=31; i<=40; i++) {
			tSource = (Task) tasks.get(30-1);
			tTarget = (Task) tasks.get(i-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

		
		for(int i=31; i<=40; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(41-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}

		for(int i=31; i<=40; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(i-1+12);
			edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		tSource = (Task) tasks.get(41-1);
		tTarget = (Task) tasks.get(42-1);
		edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
		g.addEdge(edge);
 	
		
		for(int i=43; i<=52; i++) {
			tSource = (Task) tasks.get(42-1);
			tTarget = (Task) tasks.get(i-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		for(int i=43; i<=52; i++) {
			tSource = (Task) tasks.get(i-1);
			tTarget = (Task) tasks.get(53-1);
			edge = new DirectedWeightedEdge(tSource,tTarget, 0);
			g.addEdge(edge);
	 		}
		
		
		return g;
	}
	
	
	public static DirectedGraph createChimera1Graph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

			
		ArrayList<Task> tasks=new ArrayList<Task>();
		
		//Create costless entry node
		Task t1 = new Task(1,  0);
		g.addVertex(t1);
		tasks.add(t1);
		
		
		for(int i=2; i<=43; i++) {
			Task t = new Task(i,  getTaskWeight(minTaskCost, maxTaskCost));
			g.addVertex(t);
			tasks.add(t);
		}
		

		for(int i=2; i<=25; i++) {
			Task t = (Task) tasks.get(i-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t, 0);
			g.addEdge(edge);
		}
		
		
		for(int i=2; i<=25; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=26; j<=37; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		
		for(int i=26; i<=37; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=38; j<=43; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		Task t44 = new Task(44,  getTaskWeight(minTaskCost, maxTaskCost));
		g.addVertex(t44);
		tasks.add(t44);

		for(int i=38; i<=43; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(44-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

	
		return g;
	}
	
	
	
	
	public static DirectedGraph createChimera2Graph(int minTaskCost, int maxTaskCost, int minCommCost, int maxCommCost, boolean verbose) {
		DirectedGraph g = new DefaultDirectedGraph(  );

			
		ArrayList<Task> tasks=new ArrayList<Task>();
		
		//Create costless entry node
		Task t1 = new Task(1,  0);
		g.addVertex(t1);
		tasks.add(t1);
		
		
		for(int i=2; i<=123; i++) {
			Task t = new Task(i,  getEdgeWeight(minCommCost, maxCommCost));
			g.addVertex(t);
			tasks.add(t);
		}
		
		
		for(int i=2; i<=73; i++) {
			Task t = (Task) tasks.get(i-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(t1,t, 0);
			g.addEdge(edge);
		
		}
		
		
		for(int i=2; i<=25; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=74; j<=75; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}

		for(int i=14; i<=37; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=76; j<=77; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		for(int i=26; i<=49; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=78; j<=79; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		for(int i=38; i<=61; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=80; j<=81; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		
		for(int i=50; i<=73; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=82; j<=83; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		
		for(int i=74; i<=75; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=84; j<=88; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
	
		
		for(int i=76; i<=77; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=87; j<=92; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
	
		for(int i=78; i<=79; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=91; j<=96;j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		
		for(int i=80; i<=81; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=95; j<=100; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		for(int i=82; i<=83; i++) {
			Task tSource = (Task) tasks.get(i-1);
			for(int j=98; j<=103; j++) {
				Task tTarget = (Task) tasks.get(j-1);
				DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
				g.addEdge(edge);
		 			}
		}
		
		
		for(int i=84; i<=103; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(i-1 + 20);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		
		Task t124 = new Task(124, getTaskWeight(minTaskCost, maxTaskCost));
		g.addVertex(t124);
		tasks.add(t124);
		
		for(int i=104; i<=123; i++) {
			Task tSource = (Task) tasks.get(i-1);
			Task tTarget = (Task) tasks.get(124-1);
			DirectedWeightedEdge edge = new DirectedWeightedEdge(tSource,tTarget, getEdgeWeight(minCommCost, maxCommCost));
			g.addEdge(edge);
	 		}
		

		return g;
	}


		
}


