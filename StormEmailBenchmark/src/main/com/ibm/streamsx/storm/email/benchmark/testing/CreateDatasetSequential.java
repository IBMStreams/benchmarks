// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.testing;

import com.ibm.streamsx.storm.email.benchmark.utils.ParseEmailFile;
import com.ibm.streamsx.storm.email.benchmark.utils.Serialization;
import com.ibm.streamsx.storm.email.benchmark.utils.WriteToDiskCompressed;


public class CreateDatasetSequential {
	
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.out.println("Usage: <input_path> <output_file_path>");
		} else {
			ParseEmailFile pEF = new ParseEmailFile(args[0]);
			WriteToDiskCompressed wTDC = new WriteToDiskCompressed(args[1]);
			Serialization serialization = new Serialization();
			String[] email = null;
			int count = 0;
			while(true) {
				email = pEF.getNextEmail();
				if(email[0] != null && email[1] != null && email[2] != null && email[3] != null
						 && email[4] != null && email[5] != null && email[6] != null
						 && email[7] != null) {
					wTDC.write(serialization.avroSerialize(email[0], email[1], email[2], email[3], 
						email[4], email[5], email[6], email[7], 0, 0, 0));
					count++;
				} else {
					break;
				}
			} 
			serialization.close();
			wTDC.close();
			System.out.println(count + " emails were serialized, compressed, and written");
		}
	}
}
