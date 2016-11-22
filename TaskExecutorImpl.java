package edu.utdallas.taskExecutorImpl;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.concurrent.ArrayBlockingQueue;
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;
import edu.utdallas.blockingFIFO.BlockingFIFOqueImpl;

public class TaskExecutorImpl implements TaskExecutor
{
	BlockingFIFOqueImpl que;
	FileWriter writer;
	PrintWriter printer;

	@Override
	public void addTask(Task task)
	{
		try {
			que.put(task);
		} catch (Exception e) {
			printer.append("****Error while adding task to que****");
			printer.append(e.getMessage());
			printer.append(e.getStackTrace().toString());
		}
	}
	
	Runnable threadimpl = new Runnable() {
		
		@Override
		public void run() {
			Task myTask;
			while(true){
				try{
					myTask = que.take();
					myTask.execute();
				}catch (Exception e) {
					printer.append("****Error while adding task to que****");
					printer.append(e.getMessage());
					printer.append(e.getStackTrace().toString());
				}
			}
		}
	};
	
	public TaskExecutorImpl(int numThreads){
		que = new BlockingFIFOqueImpl(100);
		try {
			writer = new FileWriter("error.log", true);
			printer = new PrintWriter(writer);
		} catch (IOException e) {
			printer.append("****Error while opening file****");
			printer.append(e.getMessage());
			printer.append(e.getStackTrace().toString());
		}
		int i = 0;
		Thread[] arr = new Thread[numThreads];
		while(i < numThreads){
			arr[i] = new Thread(this.threadimpl,(""+i+""));
			arr[i].start();
			i++;
		}
		
	}
}
