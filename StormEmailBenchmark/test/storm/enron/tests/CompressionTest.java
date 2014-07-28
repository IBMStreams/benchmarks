package storm.enron.tests;

import java.io.IOException;

import junit.framework.TestCase;
import storm.enron.utils.Compression;

public class CompressionTest extends TestCase {
	
	public CompressionTest(String name) {
		super(name);
	}
	
	public void testCompression() {
		String str = "Hello! I shall be compressed";
		try {
			String decompressed 
			= new String(Compression.decompress(Compression.compress(str.getBytes())));
			assertEquals(str, decompressed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}