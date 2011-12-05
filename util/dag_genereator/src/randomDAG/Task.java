/*
 * Created on 06/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package randomDAG;

import java.util.ArrayList;


public class Task {
	
	public int number;
	public int lines;
	public double peso;
	public double startTime;
	public double EstimatedNeededFinishTime;
	public int grupo;
	public double processamento;
	public int fork;
	public boolean escalonada;
	public boolean STcalculado;
	public double startTimeMaq;
	public ArrayList controlador;
    public int recurso;
    boolean inCP;
    boolean running;
    double EFT;
    double RFT;
    boolean finished;
    boolean z;
    double processamentoOriginal;
    boolean ready;
    boolean roundLimit;
    int processNum;
    boolean scheduledConcurrent;
    int maxLevel;
    boolean concatenada;
    boolean agrupada;
    int processNumOriginal;
    boolean separadamente;
    boolean groupTwoInterleave;
	
	public Task(int n, int l)
	{
		number = n;
		lines = l;
		peso = -1;
		startTime = -1;
		EstimatedNeededFinishTime = -1;
		grupo = -1;
		fork=-1;
		processamento = 100;
		escalonada = false;
		STcalculado = false;
		startTimeMaq=-1;
		this.controlador=controlador;
		recurso = -1;
		inCP = false;
		running = false;
		EFT=-1;
		RFT=-1;
		finished=false;
		processamentoOriginal=processamento;
		ready=false;
		roundLimit=false;
		processNum=-1;
		scheduledConcurrent=false;
		maxLevel=0;
		processNumOriginal=processNum;
		agrupada=false;
		concatenada=false;
		separadamente=false;
		groupTwoInterleave=false;
	}
	
	public void clear(ArrayList controlador,int l, int numberProcess) {
		lines = l;
		peso = -1;
		startTime = -1;
		EstimatedNeededFinishTime = -1;
		grupo = -1;
		fork=-1;
		processamento = 100;
		escalonada = false;
		STcalculado = false;
		startTimeMaq=-1;
		this.controlador=controlador;
		recurso = -1;
		inCP = false;
		running = false;
		EFT=-1;
		RFT=-1;
		finished=false;
		processamentoOriginal=processamento;
		ready=false;
		roundLimit=false;
		processNum=numberProcess;
		scheduledConcurrent=false;
		processNumOriginal=processNumOriginal;
				
	}

}
