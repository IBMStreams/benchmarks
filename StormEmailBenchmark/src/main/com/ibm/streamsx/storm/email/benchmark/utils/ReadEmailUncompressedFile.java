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

public class ReadEmailUncompressedFile extends ReadEmailFile {
	
	public ReadEmailUncompressedFile(String filepath) throws IOException {
		super(filepath, new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(filepath)))));
	}
}