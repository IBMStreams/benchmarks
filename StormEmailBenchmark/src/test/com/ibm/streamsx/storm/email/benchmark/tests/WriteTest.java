// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.tests;

import com.ibm.streamsx.storm.email.benchmark.utils.Misc;

import junit.framework.TestCase;

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