// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
//  
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadEmailFile {
	
	protected ObjectInputStream inputStream;
	private byte[] read;
	
	public ReadEmailFile(String filepath, ObjectInputStream is) throws IOException {
		inputStream = is;
	}
	
	public byte[] getNextEmail() throws IOException, ClassNotFoundException {
		read = null;
		if(inputStream != null) {
			try {
				// using readUnshared instead of readObject to ensure there is no memory leak!				
				read = (byte[])inputStream.readUnshared();								
			} catch(EOFException e) {
				//System.out.println("End of input file!");
				inputStream.close();
				inputStream = null;
			}
		}
		return read;
	}	
}