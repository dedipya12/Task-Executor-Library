package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;

public interface BlockingFIFOque {

	void put(Task item) throws Exception;
    Task take() throws Exception;

}
