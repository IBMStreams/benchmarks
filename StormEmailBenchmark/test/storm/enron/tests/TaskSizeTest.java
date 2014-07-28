package storm.enron.tests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import storm.enron.utils.TaskQueue;

public class TaskSizeTest extends TestCase {

	public TaskSizeTest(String name) {
		super(name);
	}
	
	public void testCompression() {
		TaskQueue taskQueue = new TaskQueue(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5)));
		for(int i = 0; i < 5; i++) {
			taskQueue.updateQueue(taskQueue.getNextTaskID(), taskQueue.getNextTaskID());
		}
		for(int i = 0; i < 5; i++) {
			assertEquals(i + 1, taskQueue.getNextTaskID());
			taskQueue.updateQueue(taskQueue.getNextTaskID(), 10);
		}
		
	}

}