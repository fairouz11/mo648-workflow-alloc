/**
 * Particle Swarm Optimization Scheduler Main Class
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Hashtable;
import java.util.Enumeration;

import br.unicamp.ic.wfscheduler.*;


public class PSOScheduler implements IScheduler
{
	/**
	 * Attributes
	 */
	private static final int SWARM_SIZE = 25;
	private static final int MAX_ITERATION = 45;
	
	private static final double COST_COMMUNICATION = 0.17;
	
	// acceleration coefficients
	private static final double C1 = 2.0;
	private static final double C2 = 2.0;
	
	private List<Task> tasks;
	private List<Host> hosts;
	//private long bandwidth;
	
	private Hashtable<Task, Hashtable<Host,Double>> TH_matrix;
	private Hashtable<Host, Hashtable<Host,Double>> HH_matrix;
	private Hashtable<Task, Hashtable<Task,Double>> edge_weight;
	
	//A list of particles
	private ArrayList<Particle> swarm;
	
	//ArrayList to save the previous best local fitness values of the particles
	private ArrayList<Double> fitnessValue;
	
	//ArrayList to save the current best local fitness values of the particles
	private ArrayList<Double> pbest;
	
	//Array that save the best local position of the particles
	private ArrayList<Position> pBestLoc;
	
	//Variable that saves the fitness value of the best particle in the swarm
	private Double gbest;
	
	//Variable that saves the position of the best particle in the swarm
	private Position gBestLoc;
	
	private Hashtable<Task,Host> assignedTasks;
	
	/**
	 * Constructor
	 */
	public PSOScheduler()
	{
		swarm = new ArrayList<Particle>();
		fitnessValue = new ArrayList<Double>();
		pbest = new ArrayList<Double>();
		pBestLoc = new ArrayList<Position>();
		gBestLoc = new Position();
	}
	
	
	public void startScheduler(Broker broker)
	{
		//Getting a list of tasks and hosts
		tasks = broker.getTasks();
		hosts = broker.getHosts();
		
		Scheduling_Heuristic(broker);
	}
	
	private void calculate_computation_cost_matrix()
	{
		TH_matrix = new Hashtable<Task, Hashtable<Host, Double>>();
		
		for(Task t : tasks)
		{
			Hashtable<Host, Double> H_cost = new Hashtable<Host, Double>();
			
			for(Host h : hosts)
				H_cost.put(h, t.getLength()*h.getCost());
					
			TH_matrix.put(t, H_cost);
		}
	}
	
	private void calculate_communication_cost_matrix()
	{
		HH_matrix = new Hashtable<Host, Hashtable<Host, Double>>();
		
		for(Host h : hosts)
		{
			Hashtable<Host, Double> H_cost = new Hashtable<Host, Double>();
			for(Host h2 : hosts){
				if(h.equals(h2)){
					H_cost.put(h2, 0.0);
				}
				else{
					H_cost.put(h2, COST_COMMUNICATION);//depois ver como modificar esse valor
				}	
			}
			HH_matrix.put(h, H_cost);
		}
	}
	
	private void calculate_edge_weight()
	{
		edge_weight = new Hashtable<Task, Hashtable<Task, Double>>();
		for(Task t1 : tasks)
		{
			List<Task> dependencies = t1.getDependencies();
			Hashtable<Task, Double> tcost = new Hashtable<Task, Double>();
			for(Task t2 : tasks)
			{
				if(dependencies.contains(t2)){
					tcost.put(t2, (double)t1.getOutputSize());
				}
				else{
					tcost.put(t2, 0.0);
				}
			}
			edge_weight.put(t1, tcost);
		}
	}
	
	private void Scheduling_Heuristic(Broker broker)
	{
		//Step 1: Calculate average computation cost of all tasks in all compute resources
		calculate_computation_cost_matrix();
		
		//Step 2: Calculate average cost of communication between resources
		calculate_communication_cost_matrix();
		
		//Step 3: Set task node weight w(k,j) as average computation cost
		//In fact, w(k,j) == TH_matrix
		
		//Step 4: Set edge weight e(k1,k2) as size of file transferred between tasks
		calculate_edge_weight();
		
		//Step 5: Compute PSO({ a set of all tasks })
		List<Task> readyTasks = new ArrayList<Task>(tasks);
		ArrayList<Host> trans = new ArrayList<Host>();
		PSO_Algorithm(readyTasks);
		
		//Step 6: Repeat a code block until there are unscheduled tasks
		do{
			readyTasks = get_readyTasks(readyTasks);
			
			//Step 10: Dispatch all the mapped tasks
			for(Task t : readyTasks){
				Host host;
				for(Host h : hosts){
					if(h.getID() == gBestLoc.getPosition().get(t)){
						host = h;
					}
				}
				
				broker.assign(t, host); 
				assignedTasks.put(t, host);
				
				// now, we have to transmit the dependencies to this host
				// as we have topologically ordered the tasks, all dependencies
				// should be assigned by now
				trans.add(host);
				
				for (Task dep : t.getDependencies())
					broker.transmitResult(dep, trans);

				trans.clear();
			}
			
			//Step 11: wait for polling_time
			
			//Step 12: Update readyTask list
			
			//Step 13: Update average cost of communication between resources
			//according to the current network load
			
			//Step 14: Compute PSO
			PSO_Algorithm(readyTasks);
			
		}while(exists_unscheduled_tasks());
		
	}
	
	private List<Task> get_readyTasks(List<Task> tasks)
	{
		
	}
	
	
	private boolean exists_unscheduled_tasks()
	{
		return true;
	}
	
	/**
	 * The PSO_Algorithm function: it receives a list with all tasks
	 * @param tasks
	 */
	private void PSO_Algorithm(List<Task> readyTasks)
	{
		/*
		 * Step 1: Set particle dimension as equal to the size of tasks list
		 */
		int particleDimension = readyTasks.size();
		
		/*
		 * Step 2: Initialize particles position randomly and velocity v randomly
		 */
		init_Swarm(readyTasks);
		
		int iteration = 0;
		while(iteration < MAX_ITERATION)
		{
			//Step 3: For each particle, calculate its fitness value
			calculateAllFitness();
		
			/*
			 * Step 4: Update pbest. If the fitness value is better than the previous 
			 * best pbest, set the current fitness value as the new pbest
			 */
			if(iteration == 0)
			{
				for(int i=0; i<SWARM_SIZE; i++)
				{
					pbest.add(i, fitnessValue.get(i));
					pBestLoc.add(i, swarm.get(i).getLocation());
				}
			}
			else
			{
				for (int j=0; j<SWARM_SIZE; j++)
				{
					 if ( fitnessValue.get(j) < pbest.get(j) )
					 {
						 pbest.set(j, fitnessValue.get(j));
						 pBestLoc.set(j, swarm.get(j).getLocation());
					 }
				}

			}
			
			/*
			 * Step 5: Select the best particle as gbest
			 */
			int bestIndex = getBestParticle();
			if(iteration == 0 || (fitnessValue.get(bestIndex) < gbest))
			{
				gbest = fitnessValue.get(bestIndex);
				gBestLoc = swarm.get(bestIndex).getLocation();
			}
		
			/*
			 * Step 6: For all particles, calculate velocity and update their positions
			 */
			double inertia = 0.75;
			
			for(int w=0; w<SWARM_SIZE; w++)
			{
				//Generates random numbers greater than 0 and less than 1
				double rand1 = Math.random();
				double rand2 = Math.random();
				
				Position previous_position = new Position();
				Velocity previous_velocity = new Velocity();
				Position pbest_position = new Position();
				Position gbest_position = new Position();
				
				for(int i=0; i<particleDimension; i++)
				{
					previous_position.addPosition(readyTasks.get(i), swarm.get(w).getLocation().getIndexValue(readyTasks.get(i)));
					previous_velocity.addVelocity(readyTasks.get(i), swarm.get(w).getVelocity().getIndexValue(readyTasks.get(i)));
					pbest_position.addPosition(readyTasks.get(i), pBestLoc.get(w).getIndexValue(readyTasks.get(i)));
					gbest_position.addPosition(readyTasks.get(i), gBestLoc.getIndexValue(readyTasks.get(i)));
				
					double value = (inertia*previous_velocity.getIndexValue(readyTasks.get(i))) + (rand1*C1)*(pbest_position.getIndexValue(readyTasks.get(i))-previous_position.getIndexValue(readyTasks.get(i))) + (rand2*C2)*(gbest_position.getIndexValue(readyTasks.get(i))-previous_position.getIndexValue(readyTasks.get(i)));
				
					swarm.get(w).getVelocity().setVelocity(readyTasks.get(i), value);
					swarm.get(w).getLocation().setPosition(readyTasks.get(i), previous_position.getIndexValue(readyTasks.get(i)) + (int)value);
				}
			}
			
			iteration++;
		}
		
	}
	
	/**
	 * Initialize the swarm with a number of particles and 
	 * set up their initial positions and velocities
	 * @param dimension
	 */
	private void init_Swarm(List<Task> readyTasks)
	{
		
		Particle particle;
		int dimension = readyTasks.size();
		Random rand = new Random();
		int numberHosts = hosts.size();
		
		//Create the swarm
		for(int i=0; i<SWARM_SIZE; i++)
		{
			particle = new Particle(dimension);
			
			//Initialize particle position and velocity with random values
			for(int j=0; j<dimension; j++)
			{
				//Each position dimension must receive a value between (1..#_of_hosts)
				int randomNum = rand.nextInt(numberHosts) + 1;
				particle.getLocation().addPosition(readyTasks.get(j), randomNum);
				//As the article does not specify the range of values to velocity, I suppose values between 1..100
				int randomNum2 = rand.nextInt(100) + 1;
				particle.getVelocity().addVelocity(readyTasks.get(j), randomNum2);
			}
			
			swarm.add(particle);
		}
	}
	/**
	 * Auxiliary function for PSO_algorithm: calculate the fitness of all particles in the swarm
	 */
	private void calculateAllFitness()
	{
		for(int i=0; i<swarm.size(); i++)
		{
			//fitnessValue.add(i, swarm.get(i).getFitness());
			double cost = Cost(swarm.get(i));
			swarm.get(i).setFitness(cost);
			fitnessValue.add(i, cost);
		}
	}
	
	/************************************
	 * Functions to calculate fitness
	 */
	private double d(Particle p, Task t1, Task t2)
	{
		Host host1, host2;
		
		for(Host h : hosts){
			if(h.getID().equals(p.getLocation().getIndexValue(t1)))
				host1 = h;
			else if(h.getID().equals(p.getLocation().getIndexValue(t2)))
				host2 = h;
		}
		
		return HH_matrix.get(host1).get(host2);
	}
	
	private double Cost(Particle p)
	{
		double max_cost = 0.0;
		for(Host h : hosts)
		{
			double cost_h = TotalCost(p, h);
			if(max_cost < cost_h){
				max_cost = cost_h;
			}
		}
		return max_cost;
	}
	
	private double TotalCost(Particle p, Host h)
	{
		return (CostAccess(p,h) + CostComputation(p,h));
	}
	
	private double CostAccess(Particle p, Host h)
	{
		double cost_access = 0;
		
		for (Enumeration<Task> e = p.getLocation().getPosition().keys() ; e.hasMoreElements() ;) {
			Task t = e.nextElement(); //ver como funciona essa funcao depois
			if(p.getLocation().getPosition().get(t).equals(h.getID()))
			{
				double partial_cost = 0;
				/**
				 * A second for
				 */
				for (Enumeration<Task> e2 = p.getLocation().getPosition().keys() ; e2.hasMoreElements() ;)
				{
					Task t2 = e2.nextElement();
					if(!(p.getLocation().getPosition().get(t2).equals(h.getID())))
					{
						partial_cost += (d(p,t,t2)*edge_weight.get(t).get(t2));
					}
				}
				cost_access += partial_cost;
			 }
		}
		return cost_access;
	}
	
	private double CostComputation(Particle p, Host h)
	{
		double w_h = 0;
		
		for (Enumeration<Task> e = p.getLocation().getPosition().keys() ; e.hasMoreElements() ;) {
			Task t = e.nextElement(); //ver como funciona essa funcao depois
			if(p.getLocation().getPosition().get(t).equals(h.getID())){
				Hashtable<Host, Double> aux_matrix = TH_matrix.get(t);
				w_h += aux_matrix.get(h);
			}
		}
		return w_h;
	}
	
	/**
	 * Auxiliary function for PSO_algorithm: returns the index of the particle with the smaller fitness value
	 */
	private int getBestParticle()
	{
		/*int bestIndex = 0;
		double value = swarm.get(0).getFitness();
		for(int i=1; i<SWARM_SIZE; i++)
		{
			if(swarm.get(i).getFitness() < value)
			{
				bestIndex = i;
				value = swarm.get(i).getFitness();
			}
		}*/
		int bestIndex = 0;
		double value = fitnessValue.get(0);
		for(int i=1; i<SWARM_SIZE; i++)
		{
			if(fitnessValue.get(i) < value)
			{
				bestIndex = i;
				value = fitnessValue.get(i);
			}
		}
		return bestIndex;
	}
	
	/**
	 * Call back when a task is finished
	 */
	public void taskFinished(Task t, Host h)
	{
		
	}


	@Override
	public void transmissionFinished(Task task, Host sender, Host destionation)
	{
		// TODO Auto-generated method stub
		
	}
}
