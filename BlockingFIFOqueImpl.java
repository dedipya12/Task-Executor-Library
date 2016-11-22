package edu.utdallas.blockingFIFO;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.utdallas.taskExecutor.Task;

public class BlockingFIFOqueImpl implements BlockingFIFOque{
	private final Task[] arr;
	private int arrSize;
	private int start;
	private int end;
	private int ocupied;
	private final Lock mlock = new ReentrantLock();
	private Condition Empty = mlock.newCondition();
	private Condition Full = mlock.newCondition();
	
	public BlockingFIFOqueImpl(int Size) {
		
		arrSize = Size;
		start = 0;
		end = 0;
		ocupied = 0;
		arr = new Task[arrSize];
	} 

	@Override
	public void put(Task item) throws Exception {
		
		mlock.lock();
		try{
			while(ocupied == arrSize) Full.await();
			ocupied++;
			arr[end++]=item;
			if(end > arrSize-1){
				end = end - arrSize;
			}
			Empty.signalAll();
		} finally{
			mlock.unlock();
		}
	}


	@Override
	public Task take() throws Exception {
		
		Task item;
		mlock.lock();
		try{
			while(ocupied == 0) Empty.await();
			ocupied = ocupied - 1;
			item = arr[start];
			start++;
			if(start > arrSize-1){
				start = start - arrSize;
			}
			Full.signal();
		}finally{
			mlock.unlock();
		}
			
		return item;
	}

}
