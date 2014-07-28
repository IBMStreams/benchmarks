// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.tests;

import java.io.IOException;

import com.ibm.streamsx.storm.email.benchmark.utils.Compression;

import junit.framework.TestCase;

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
			e.printStackTrace();
		}
	}
}