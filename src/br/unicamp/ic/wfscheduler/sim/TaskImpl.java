package br.unicamp.ic.wfscheduler.sim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import br.unicamp.ic.wfscheduler.Task;

class TaskImpl implements Task
{
	private static int cloudletID = 0;
	private static UtilizationModel utilizationMode = new UtilizationModelFull();
	
	private Cloudlet cloudlet;
	private ArrayList<Task> dependencies;

	public TaskImpl(long length, long outputSize, BrokerImpl broker)
	{
		cloudlet = new Cloudlet(cloudletID++, 
				length, 1, 0, outputSize, utilizationMode, 
				utilizationMode, utilizationMode);
		cloudlet.setUserId(broker.getUserID());
		
		dependencies = new ArrayList<Task>();
	}
	
	Cloudlet getCsCloudlet()
	{
		return cloudlet;
	}
	
	/**
	 * Get processing cost if task was executed on host h
	 */
	double calculateProcessingCost(HostImpl h)
	{
		return cloudlet.getActualCPUTime() * h.getCost();
	}
	
	/**
	 * Add a dependent taks.
	 * @param t dependent task
	 */
	void addDependencie(Task t)
	{
		dependencies.add(t);
	}
	
	@Override
	public long getLength()
	{
		return cloudlet.getCloudletLength();
	}

	@Override
	public List<Task> getDependencies()
	{ 
		return new ArrayList<Task>(dependencies);
	}

	@Override
	public long getOutputSize()
	{
		return cloudlet.getCloudletOutputSize();
	}

}
