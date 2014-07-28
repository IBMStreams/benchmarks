package storm.enron.tests;

import storm.enron.utils.MetricCalculation;
import junit.framework.TestCase;

public class MetricCalculationTest extends TestCase {
	
	public MetricCalculationTest(String name) {
		super(name);
	}
	
	public void testCharCountOld() {
		String text = "This is our test string";
		assertEquals(19, MetricCalculation.getCharCountOld(text));
	}
	
	public void testCharCount() {
		String text = "This is our test string";
		assertEquals(23, MetricCalculation.getCharCount(text));
	}
	
	public void testWordCount() {
		String text = "This is our test  string";
		assertEquals(5, MetricCalculation.getWordCount(text));
	}
	
	public void testParagraphCount() {
		String text = "This is our test string\nThis is our test string\nThis is our test string";
		assertEquals(3, MetricCalculation.getParagraphCount(text));
	}
	
	public void testWordAndParagraphCountOld() {
		String text = "This is our test string\nThis is our test string\nThis is our test string";
		int[] counts = MetricCalculation.getWordAndParagraphCountOld(text);
		assertEquals(15, counts[0]);
		assertEquals(3, counts[1]);
	}
	
	public void testWordAndParagraphCount() {
		String text = "This is our test string\nThis is our test string\nThis is our test string";
		int[] counts = MetricCalculation.getWordAndParagraphCount(text);
		assertEquals(15, counts[0]);
		assertEquals(3, counts[1]);
	}

}