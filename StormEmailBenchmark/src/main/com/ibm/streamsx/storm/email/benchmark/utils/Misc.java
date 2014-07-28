// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
//  
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import backtype.storm.tuple.Fields;

public class Misc {
	
	public static void writeToDisk(byte[] data, String path) throws IOException {
		ObjectOutputStream fileStream = new ObjectOutputStream(new FileOutputStream(path, true));
        fileStream.write(data);
        fileStream.close();
	}
	
	public static void writeToDisk(byte[] data) throws IOException {
		writeToDisk(data, ConfigProps.getProperty(Constants.OUTPUT_PATH));
	}			
	
	public static byte[] delimitArray(byte[] data) {
		byte[] newData = new byte[data.length + 3];
		newData[0] = '{';		
		newData[newData.length - 1] = '}';
		newData[newData.length - 2] = '$';
		System.arraycopy(data, 0, newData, 1, data.length);
		return newData;
	}
	
	public static File streamToTempFile(InputStream inputStream) throws IOException {
		File file = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
		FileOutputStream outputStream = new FileOutputStream(file);			
		int read = 0;
		
		byte[] bytes = new byte[1024];
		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		outputStream.close();
		inputStream.close();
		return file;
	}
	
	public static String[] tokenizeEmail(String text) {
		String[] tokens = new String[8];
		int index = 0;
		if(text != null && text.length() > 1) {
			for(String token: text.split("\", ")) {
				tokens[index++] = token.substring(token.indexOf("=\"") + 2);
			}
		}
		
		return tokens;
	}
	
	public static String[] tokenizeEmailNew(String text) {
		String[] tokens = new String[8];
		int index = 0;
		if(text != null && text.length() > 1) {
			String s[] = text.split("From=\"|\\b(?<!-)Date=\"|Subject=\"|ToList=\"|CcList=\"|BccList=\"|Body=\"");
			if(s.length < 8) {
				return tokens;
			}
			for(String token: s) {
				if(index < 7) {
					if(index == 0) {
						// first field
						String newToken = token.split("ID=\"")[1];
						tokens[index] = newToken.substring(1, newToken.length() - 4);
					} else {
						// other fields
						tokens[index] = token.substring(0, token.length() - 3);
					}
				} else if (index == 7){
					// body, so no need to trim
					tokens[index] = token;
				} else {
					// append to body
					tokens[7] += token;
				}
				index++;
			}
		}
		return tokens;
	}
	
	public static String cleanFromField(String text) {
		return text.split(" ")[0];
	}
	
	public static Fields getEmailFields() {
		return new Fields("id", "from", "date", "subject", "tolist", "cclist", 
				"bcclist", "body", "charcount", "wordcount", "paracount");
	}

}
