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

public class WriteToDiskUncompressed extends WriteToDisk {

	public WriteToDiskUncompressed(String path) throws FileNotFoundException, IOException {
		super(path, new ObjectOutputStream(new FileOutputStream(path)));
	}
	
	public WriteToDiskUncompressed() throws IOException {
		this(ConfigProps.getProperty(Constants.OUTPUT_PATH));
	}
	
}