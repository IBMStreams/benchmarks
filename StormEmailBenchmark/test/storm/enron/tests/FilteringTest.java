package storm.enron.tests;

import storm.enron.utils.Filtering;
import junit.framework.TestCase;

public class FilteringTest extends TestCase {
	
	private Filtering filtering;
	
	public FilteringTest(String name) {
		super(name);
		filtering = new Filtering();
	}
	
	public void testFromFiltering() {
		String emailEnron = "john.doe@enron.com";
		String emailNonEnron = "jane.doe@somedomain.com";
		assertEquals(true, filtering.checkFromEnron(emailEnron));
		assertEquals(false, filtering.checkFromEnron(emailNonEnron));
	}
	
	public void testEmailFiltering() {
		String emailList = "one@enron.com, two@enron.com, three@gmail.com, " +
				"four@enron.com, five@gmail.com, six@enron.com";
		String filteredList = "one@enron.com, two@enron.com, " +
				"four@enron.com, six@enron.com";
		assertTrue(filtering.removeEnronEmail(emailList).equals(filteredList));

	}
	
	public void testEmailFilteringNoList() {
		String emailList = "one@enron.com, two@enron.com, three@gmail.com, " +
				"four@enron.com, five@gmail.com, six@enron.com";
		String filteredList = "one@enron.com, two@enron.com, " +
				"four@enron.com, six@enron.com";
		assertTrue(filtering.removeEnronEmailNoList(emailList).equals(filteredList));

	}
	
	public void testEmailFilteringBuilder() {
		String emailList = "one@enron.com, two@enron.com, three@gmail.com, " +
				"four@enron.com, five@gmail.com, six@enron.com";
		String filteredList = "one@enron.com, two@enron.com, " +
				"four@enron.com, six@enron.com";
		assertTrue(filtering.removeEnronEmailBuilder(emailList).equals(filteredList));

	}
	
	public void testTextJunk() {
		final String text = "Dear Eric,  Growing developments in Washington regarding Enron =\n" + 
				"- second hearing has been added for this Thursday.  A complete list of who =\n" +
				" will testify remains up in the air.  We'll have all the details on hearings=\n"+ 
				" in the nation's capital from our Washington bureau on Tuesday.   A key Dem=\n" +
				"ocrat in the U.S. Senate is taking the offensive Tuesday in the debate over=\n" + 
				" national energy policy.  Days after the President renews his call for the =\n" + 
				"Senate to act on his energy plan, Senator John Kerry (D-MA) is scheduled to=\n" + 
				" deliver a speech on energy to the Center for National Policy.  Kerry will =\n" + 
				"outline his ideas on how to reduce U.S. dependence on foreign oil and voice=\n" + 
				" his criticism of the Bush administration's push to open a portion of Alask=\n" + 
				"a's Arctic National Wildlife Refuge to exploration and production.    A fed=\n" + 
				"eral court judge will hear arguments Tuesday regarding the destruction of E=\n" + 
				"nron-related documents.  The hearing is in response to Amalgamated Bank's r=\n" + 
				"equest to discover how and why Andersen destroyed Enron-related documents. =\n" + 
				" We'll have details on the hearing from the Houston bureau tomorrow.   Also=\n" + 
				" from Houston on Tuesday, AES NewEnergy President Clem Palevich discusses d=\n" + 
				"eregulation and overcoming problems in the Texas market.  Join us for these=\n" + 
				" reports from Houston.   Tuesday, West Coast Correspondent Kym McNicholas w=\n" + 
				"ill discuss the California ISO's new Market Design Project 2002 with Harvar=\n" + 
				"d Economist William Hogan.  Hogan says the new plan is a step forward for t=\n" + 
				"he California electricity market.  Kym speaks with Hogan during the 3:00 p.=\n" + 
				"m. ET newscast tomorrow.   Deutsche Banc Alex Brown Major Oil Analyst David=\n" + 
				" Wheeler is also dropping by on Tuesday.  Wheeler will discuss fourth-quart=\n" + 
				"er estimates for the major oil companies live from New York during the 9:00=\n" + 
				" a.m. ET newscast tomorrow.   Wrap Note: The NYSE was closed Monday due to =\n" + 
				"the Martin Luther King, Jr. holiday.  We'll continue with commodity settles=\n" + 
				" on Tuesday when the markets resume trading.   Keep in mind things are subj=\n" + 
				"ect to change at a moment's notice. Occasionally guests have to reschedule =\n" + 
				"or change time slots.  We'll continue to do our best to keep you updated on=\n" + 
				" future interviews and events.     Be sure to watch our newscasts every bus=\n" + 
				"iness day - 9 a.m. to 5 p.m. ET, at the top of each hour.  =20\n =20\n=20\n=20\n=20\n";
		final String cleanText = "Dear Eric,  Growing developments in Washington regarding Enron " + 
				"- second hearing has been added for this Thursday.  A complete list of who " +
				" will testify remains up in the air.  We'll have all the details on hearings"+ 
				" in the nation's capital from our Washington bureau on Tuesday.   A key Dem" +
				"ocrat in the U.S. Senate is taking the offensive Tuesday in the debate over" + 
				" national energy policy.  Days after the President renews his call for the " + 
				"Senate to act on his energy plan, Senator John Kerry (D-MA) is scheduled to" + 
				" deliver a speech on energy to the Center for National Policy.  Kerry will " + 
				"outline his ideas on how to reduce U.S. dependence on foreign oil and voice" + 
				" his criticism of the Bush administration's push to open a portion of Alask" + 
				"a's Arctic National Wildlife Refuge to exploration and production.    A fed" + 
				"eral court judge will hear arguments Tuesday regarding the destruction of E" + 
				"nron-related documents.  The hearing is in response to Amalgamated Bank's r" + 
				"equest to discover how and why Andersen destroyed Enron-related documents. " + 
				" We'll have details on the hearing from the Houston bureau tomorrow.   Also" + 
				" from Houston on Tuesday, AES NewEnergy President Clem Palevich discusses d" + 
				"eregulation and overcoming problems in the Texas market.  Join us for these" + 
				" reports from Houston.   Tuesday, West Coast Correspondent Kym McNicholas w" + 
				"ill discuss the California ISO's new Market Design Project 2002 with Harvar" + 
				"d Economist William Hogan.  Hogan says the new plan is a step forward for t" + 
				"he California electricity market.  Kym speaks with Hogan during the 3:00 p." + 
				"m. ET newscast tomorrow.   Deutsche Banc Alex Brown Major Oil Analyst David" + 
				" Wheeler is also dropping by on Tuesday.  Wheeler will discuss fourth-quart" + 
				"er estimates for the major oil companies live from New York during the 9:00" + 
				" a.m. ET newscast tomorrow.   Wrap Note: The NYSE was closed Monday due to " + 
				"the Martin Luther King, Jr. holiday.  We'll continue with commodity settles" + 
				" on Tuesday when the markets resume trading.   Keep in mind things are subj" + 
				"ect to change at a moment's notice. Occasionally guests have to reschedule " + 
				"or change time slots.  We'll continue to do our best to keep you updated on" + 
				" future interviews and events.     Be sure to watch our newscasts every bus" + 
				"iness day - 9 a.m. to 5 p.m. ET, at the top of each hour.  \n \n\n\n\n";								
		assertTrue(filtering.removeJunk(text).equals(cleanText));

	}
	
	/*public void testPhoneJunk() {
		final String text = "This string has an extension: x12345";
		final String cleanText = "This string has an extension: ";
		assertTrue(Filtering.removeJunk(text).equals(cleanText));
	}*/
}