// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
//  
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

public class WriteToDiskCompressed extends WriteToDisk {

	public WriteToDiskCompressed(String path) throws FileNotFoundException, IOException {
		super(path, new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(path))));
	}
	
	public WriteToDiskCompressed() throws IOException {
		this(ConfigProps.getProperty(Constants.OUTPUT_PATH));
	}
	
}