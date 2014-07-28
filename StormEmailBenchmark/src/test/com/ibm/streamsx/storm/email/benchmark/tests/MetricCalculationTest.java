// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.tests;

import com.ibm.streamsx.storm.email.benchmark.utils.MetricCalculation;

import junit.framework.TestCase;

public class MetricCalculationTest extends TestCase {
	
	private String text1 = "This is our test string";
	private String text2 = "This is our test string\nThis is our test string\nThis is our test string";
	
	public MetricCalculationTest(String name) {
		super(name);
	}
	
	public void testCharCountOld() {
		assertEquals(19, MetricCalculation.getCharCountOld(text1));
	}
	
	public void testCharCount() {
		assertEquals(23, MetricCalculation.getCharCount(text1));
	}
	
	public void testWordCount() {
		assertEquals(5, MetricCalculation.getWordCount(text1));
	}
	
	public void testParagraphCount() {
		assertEquals(3, MetricCalculation.getParagraphCount(text2));
	}
	
	public void testWordAndParagraphCountOld() {
		int[] counts = MetricCalculation.getWordAndParagraphCountOld(text2);
		assertEquals(15, counts[0]);
		assertEquals(3, counts[1]);
	}
	
	public void testWordAndParagraphCount() {
		int[] counts = MetricCalculation.getWordAndParagraphCount(text2);
		assertEquals(15, counts[0]);
		assertEquals(3, counts[1]);
	}

}