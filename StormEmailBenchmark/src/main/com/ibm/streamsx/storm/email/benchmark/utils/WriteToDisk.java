// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteToDisk {
	
	private ObjectOutputStream fileStream;

	public WriteToDisk(String path, ObjectOutputStream os) throws FileNotFoundException, IOException {
		fileStream = os;
	}
	
	public WriteToDisk(ObjectOutputStream os) throws IOException {
		this(ConfigProps.getProperty(Constants.OUTPUT_PATH), os);
	}
	
	public void write(byte[] data) throws IOException {
		fileStream.writeObject(data);
		fileStream.flush();
		// This is necessary otherwise ObjectOutputStream will keep a reference
		// for each object that it has seen so far! Stupid Java quirk!
		fileStream.reset();
	}	
	
	public void close() throws IOException {
		fileStream.close();
	}
	
}