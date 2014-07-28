// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.IOException;

public class CustomLogger implements AbstractLogger {
	
	public static CustomLogger getLogger(String jobName, String fileName) throws IOException {
		if(ConfigProps.getProperty(Constants.STORM_VERSION).equals("0.8.2")) {
			return new Log4JLogger(jobName, fileName);
		} else {
			return new LogbackLogger(jobName, fileName);
		}
	}

	@Override
	public void info(Object msg) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void error(Object msg) {
		// TODO Auto-generated method stub		
	}
	
}
