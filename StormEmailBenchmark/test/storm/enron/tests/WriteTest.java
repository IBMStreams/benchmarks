package storm.enron.tests;

import junit.framework.TestCase;
import storm.enron.utils.Misc;

public class WriteTest extends TestCase {
	
	public WriteTest(String name) {
		super(name);
	}
	
	public void testDelimitArray() {
		String str = "Hello! I shall be compressed";		
		String delimitedStr = new String(Misc.delimitArray(str.getBytes()));
		assertTrue(delimitedStr.equals("{Hello! I shall be compressed$}"));
	}
	
}