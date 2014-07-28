// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.testing;

import com.ibm.streamsx.storm.email.benchmark.utils.ParseEmailFile;
import com.ibm.streamsx.storm.email.benchmark.utils.WriteToDiskCompressed;


public class CreateCompressedDatasetSequential {
	
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.out.println("Usage: <input_path> <output_file_path>");
		} else {
			ParseEmailFile pEF = new ParseEmailFile(args[0]);
			WriteToDiskCompressed wTDC = new WriteToDiskCompressed(args[1]);
			byte[] email = null;
			int count = 0;
			while(true) {
				email = pEF.getNextEmailRaw();
				if(email != null) {
					wTDC.write(email);
					count++;
				} else {
					break;
				}
			} 
			wTDC.close();
			System.out.println(count + " emails were compressed, and written");
		}
	}
}
