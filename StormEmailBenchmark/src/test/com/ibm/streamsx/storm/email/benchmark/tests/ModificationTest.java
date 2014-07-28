// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.tests;

import com.ibm.streamsx.storm.email.benchmark.utils.Modification;

import junit.framework.TestCase;

public class ModificationTest extends TestCase {
	
	private Modification modification;
	
	public ModificationTest(String name) {
		super(name);
		modification = new Modification();
	}
	
	public void testReplacement() {
		final String[] originalNames = {"Jeffrey Skilling", "Kenneth Lay", "Andrew Fastow"};
		final String[] aliases = {"Person 1", "Person 2", "Person 3"};
		
		String text = "Jeffrey Skilling needs to be replaced as well as Kenneth Lay and Andrew Fastow";
		String textReplaced = "Person 1 needs to be replaced as well as Person 2 and Person 3";
		assertTrue(textReplaced.equals(Modification.replaceNames(text, originalNames, aliases)));

	}
	
	public void testMostFrequent() {
		final String text = "Most is the most frequently occuring word in this sentence. Why most? Because I said so!";		
		assertTrue("most".equals(modification.getMostFrequentWord(text)));

	}
	
}