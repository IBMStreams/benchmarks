package storm.enron.tests;

import storm.enron.utils.Modification;
import junit.framework.TestCase;

public class ModificationTest extends TestCase {
	
	private Modification modification;
	
	public ModificationTest(String name) {
		super(name);
		modification = new Modification();
	}
	
	public void testReplacement() {
		final String[] originalNames = {"Jeffrey Skilling", "Kenneth Lay", "Andrew Fastow"};
		final String[] aliases = {"Suspect 1", "Suspect 2", "Suspect 3"};
		
		String text = "Jeffrey Skilling needs to be replaced as well as Kenneth Lay and Andrew Fastow";
		String textReplaced = "Suspect 1 needs to be replaced as well as Suspect 2 and Suspect 3";
		assertTrue(textReplaced.equals(Modification.replaceNames(text, originalNames, aliases)));

	}
	
	public void testMostFrequent() {
		final String text = "Most is the most frequently occuring word in this sentence. Why most? Because I said so!";		
		assertTrue("most".equals(modification.getMostFrequentWord(text)));

	}
	
}