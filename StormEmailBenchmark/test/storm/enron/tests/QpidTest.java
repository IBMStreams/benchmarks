package storm.enron.tests;

import javax.jms.JMSException;

import storm.enron.utils.QpidConsumer;
import storm.enron.utils.QpidMessage;
import storm.enron.utils.QpidProducer;
import junit.framework.TestCase;

public class QpidTest extends TestCase {
	
	QpidProducer producer;
	QpidConsumer consumer;
	
	@Override protected void setUp() throws Exception {
        super.setUp();
        String URI = "amqp://guest:guest@test/?brokerlist='tcp://localhost:5672'";
        producer = new QpidProducer(URI);
        consumer = new QpidConsumer(URI);
    }

	@Override protected void tearDown() throws Exception {
        super.tearDown();
        producer.teardown();
        consumer.teardown();
    }
	
	public QpidTest(String name) {
		super(name);
	}
	
	public void testQpidBlocking() {
		try {
			String str = "Hello! I'm the test String";
			producer.putMessage(str.getBytes(), "id");
			QpidMessage m = consumer.getMessageBlocking();
			assertEquals("id", m.getID());
			assertTrue(str.equals(new String(m.getMessage())));
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void testQpidNonBlocking() throws InterruptedException {
		try {
			String str = "Hello! I'm the test String";
			producer.putMessage(str.getBytes(), "id");
			Thread.sleep(1000);
			QpidMessage m = consumer.getMessageNonBlocking();
			assertEquals("id", m.getID());
			assertTrue(str.equals(new String(m.getMessage())));
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}