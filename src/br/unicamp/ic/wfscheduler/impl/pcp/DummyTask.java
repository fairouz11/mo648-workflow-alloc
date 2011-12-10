package br.unicamp.ic.wfscheduler.impl.pcp;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.ic.wfscheduler.Task;

public class DummyTask implements Task {

	private ArrayList<Task> dependencies;
	
	public DummyTask() {
		super();
		dependencies = new ArrayList<Task>();
	}

	public ArrayList<Task> getDependencies() {
		return dependencies;
	}

	public void setDependencies(ArrayList<Task> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public long getLength() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public long getOutputSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasDependencies() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
