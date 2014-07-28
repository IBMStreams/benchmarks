// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.tests;

import java.io.IOException;

import com.ibm.streamsx.storm.email.benchmark.utils.ReadEmailCompressedFile;
import com.ibm.streamsx.storm.email.benchmark.utils.WriteToDiskCompressed;

import junit.framework.TestCase;

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