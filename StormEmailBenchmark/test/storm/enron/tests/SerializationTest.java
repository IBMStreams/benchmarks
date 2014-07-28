package storm.enron.tests;

import java.io.IOException;

import storm.enron.avro.Email;
import storm.enron.utils.Deserialization;
import storm.enron.utils.Serialization;
import junit.framework.TestCase;

public class SerializationTest extends TestCase {
	
	private Serialization serialization;
	private Deserialization deserialization;
	
	public SerializationTest(String name) {
		super(name);
		serialization = new Serialization();
		deserialization = new Deserialization();
	}
	
	public void testSerialization() {
		Email testEmail = new Email();
		testEmail.setBccList("bcc");
		testEmail.setBody("bodyyy");
		testEmail.setID("id");
		testEmail.setFrom("from");
		testEmail.setDate("date");
		testEmail.setToList("to");
		testEmail.setCharCount(10);
		testEmail.setCcList("cc");
		testEmail.setParaCount(100);
		testEmail.setSubject("subject");
		testEmail.setWordcount(1000);
		try {
			Email deserializedEmail 
			= deserialization.avroDeserialize(serialization.avroSerialize(testEmail));
			assertEquals(testEmail, deserializedEmail);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				serialization.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}