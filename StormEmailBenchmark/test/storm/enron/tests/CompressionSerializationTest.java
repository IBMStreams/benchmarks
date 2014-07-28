package storm.enron.tests;

import java.io.IOException;

import junit.framework.TestCase;
import storm.enron.utils.ReadEmailCompressedFile;
import storm.enron.utils.WriteToDiskCompressed;

public class CompressionSerializationTest extends TestCase {
	
	public CompressionSerializationTest(String name) {
		super(name);
	}
	
	public void testCompressDelimit() throws IOException, ClassNotFoundException {
		String str = "Hello! I shall be compressed";		
		String path = "/tmp/testCompressDelimit.test";
		WriteToDiskCompressed wTDC = new WriteToDiskCompressed(path);
		wTDC.write(str.getBytes());
		wTDC.close();
		ReadEmailCompressedFile pEF = new ReadEmailCompressedFile(path);	
		assertTrue(new String(pEF.getNextEmail()).equals(str));		
	}
}