/**
 * Particle Swarm Optimization Scheduler Main Class
 */
package br.unicamp.ic.wfscheduler.impl.pso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.unicamp.ic.wfscheduler.*;


public class PSOScheduler implements IScheduler, Constants
{
	/**
	 * Attributes
	 */
	private List<Task> tasks;
	private List<Host> hosts;
	private long bandwidth;
	
	//A list of particles
	private ArrayList<Particle> swarm;
	//ArrayList to save the current best local fitness values of the particles
	private ArrayList<Double> pbest;
	//ArrayList to save the previous best local fitness values of the particles
	private ArrayList<Double> fitnessValue;
	//Array that save the best local position of the particles
	private ArrayList<Position> pBestLoc;
	//Variable that saves the fitness value of the best particle in the swarm
	private Double gbest;
	//Variable that saves the position of the best particle in the swarm
	private Position gBestLoc;
	
	/**
	 * Constructor
	 */
	public PSOScheduler()
	{
		pbest = new ArrayList<Double>();
		swarm = new ArrayList<Particle>();
		pBestLoc = new ArrayList<Position>();
	}
	
	
	public void startScheduler(Broker broker)
	{
		
	}
	
	public void Scheduling_Heuristic()
	{
		//Step 1: Calculate average computation cost of all tasks in all compute resources
		
		//Step 2: Calculate average cost of communication between resources
		
		//Step 3: Set task node weight w(k,j) as average computation cost
		
		//Step 4: Set edge weight e(k1,k2) as size of file transferred between tasks
		
		//Step 5: Compute PSO({ a set of all tasks })
		PSO_Algorithm(tasks);
		
		//Step 6: Repeat a code block until there are unscheduled tasks
		
	}
	
	/**
	 * The PSO_Algorithm function: it receives a list with all tasks
	 * @param tasks
	 */
	public void PSO_Algorithm(List<Task> tasks)
	{
		/*
		 * Step 1: Set particle dimension as equal to the size of ready tasks in "tasks"
		 */
		int particleDimension = 999999;//Tenho que ver o que colocar aqui....
		
		/*
		 * Step 2: Initialize particles position randomly and velocity v randomly
		 */
		init_Swarm(particleDimension);
		
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
				for (int j = 0; j<SWARM_SIZE; j++)
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
			int inertia = 9999999; //Preciso ver o que colocar aqui.....
			
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
					previous_position.addPosition(i, swarm.get(w).getLocation().getIndexValue(i));
					previous_velocity.addVelocity(i, swarm.get(w).getLocation().getIndexValue(i));
					pbest_position.addPosition(i, pBestLoc.get(w).getIndexValue(i));
					gbest_position.addPosition(i, gBestLoc.getIndexValue(i));
				}
				
				Velocity new_velocity = new Velocity();
				Position new_position = new Position();
				
				for(int j=0; j<particleDimension; j++)
				{
					double value = (inertia*previous_velocity.getIndexValue(j)) + (rand1*C1)*(pbest_position.getIndexValue(j)-previous_position.getIndexValue(j)) + (rand2*C2)*(gbest_position.getIndexValue(j)-previous_position.getIndexValue(j));
					new_velocity.addVelocity(j, value);
					new_position.addPosition(j, previous_position.getIndexValue(j) + new_velocity.getIndexValue(j));
				}
				swarm.get(w).setVelocity(new_velocity);
				swarm.get(w).setLocation(new_position);
			}
			
			iteration++;
		}
		
	}
	
	/**
	 * Initialize the swarm with a number of particles and 
	 * set up their initial positions and velocities
	 * @param dimension
	 */
	private void init_Swarm(int dimension)
	{
		
		Particle particle;
		Position position;
		Velocity velocity;
		Random rand = new Random();
		int numberHosts = hosts.size();
		
		//Create the swarm
		for(int i=0; i<SWARM_SIZE; i++)
		{
			particle = new Particle();
			position = new Position();
			velocity = new Velocity();
			
			//Initialize particle position and velocity with random values
			for(int j=0; j<dimension; j++)
			{
				//Each position dimension must receive a value between (1..#_of_hosts)
				int randomNum = rand.nextInt(numberHosts) + 1;
				position.addPosition(j, randomNum);
				//As the article does not specify the range of values to velocity, I suppose values between 1..100
				int randomNum2 = rand.nextInt(100) + 1;
				velocity.addVelocity(j, randomNum2);
			}
			
			particle.setLocation(position);
			particle.setVelocity(velocity);
			
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
			fitnessValue.add(i, swarm.get(i).getFitness());
		}
	}
	
	/**
	 * Auxiliary function for PSO_algorithm: returns the index of the particle with the smaller fitness value
	 */
	private int getBestParticle()
	{
		int bestIndex = 0;
		double value = swarm.get(0).getFitness();
		for(int i=1; i<SWARM_SIZE; i++)
		{
			if(swarm.get(i).getFitness() < value)
			{
				bestIndex = i;
				value = swarm.get(i).getFitness();
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
}
