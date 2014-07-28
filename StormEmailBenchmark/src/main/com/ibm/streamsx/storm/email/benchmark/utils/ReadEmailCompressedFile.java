// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

public class ReadEmailCompressedFile extends ReadEmailFile {
	
	public ReadEmailCompressedFile(String filepath) throws IOException {
		super(filepath, new ObjectInputStream(new GZIPInputStream (new BufferedInputStream(new FileInputStream(new File(filepath))))));
	}
}