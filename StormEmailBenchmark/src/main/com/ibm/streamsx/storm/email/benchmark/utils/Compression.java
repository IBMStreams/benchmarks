// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import backtype.storm.tuple.Tuple;

public class Compression {
	
	public static byte[] compress(byte[] data) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
		gzipOutputStream.write(data);
	    gzipOutputStream.close();
	    return byteArrayOutputStream.toByteArray();
	}
	
	public static byte[] compress(Tuple input) throws IOException {
		return compress(input.getBinary(0));
	}
	
	public static byte[] decompress(byte[] compressed) throws IOException {
		GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(compressed));
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int bytesRead;
		byte[] decompressedEmail = new byte[4096];

		while ((bytesRead = inputStream.read(decompressedEmail, 0, decompressedEmail.length)) != -1) {
		  buffer.write(decompressedEmail, 0, bytesRead);
		}

		buffer.flush();
		byte [] byteArray = buffer.toByteArray();
		inputStream.close();
		buffer.close();
		return byteArray;
   }
	
}
