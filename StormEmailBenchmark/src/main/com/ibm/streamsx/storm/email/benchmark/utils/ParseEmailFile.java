// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
//  
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParseEmailFile {
	
	protected Scanner emailFileScanner;
	private String email;
	private String rawEmail;
	
	public ParseEmailFile(String filepath) throws FileNotFoundException {
		emailFileScanner = new Scanner(new BufferedInputStream(new FileInputStream(new File(filepath))));
		emailFileScanner.useDelimiter("\"}\\n(?!\\n)");
	}

	public String getNextEmailString() {
		if(emailFileScanner.hasNext()) {
			email = emailFileScanner.next();
			return email;
		}
		emailFileScanner.close();
		return null;
	}
	
	public String[] getNextEmail() {
		return Misc.tokenizeEmailNew(getNextEmailString());
	}
	
	public byte[] getNextEmailRaw() {
		rawEmail = getNextEmailString();
		if(rawEmail != null) {
			return rawEmail.getBytes();
		}
		return null;
	}		
	
}