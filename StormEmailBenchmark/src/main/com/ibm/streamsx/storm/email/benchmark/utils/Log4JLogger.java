// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4JLogger extends CustomLogger {
	
	private Logger logger;
	
	public Log4JLogger(String jobName, String fileName) throws IOException {
		logger = Logger.getLogger(fileName);
		logger.removeAllAppenders();
		logger.addAppender(getFileAppender(jobName, fileName));
	}
	
	private FileAppender getFileAppender(String jobName, String fileName) throws IOException {
		FileAppender fileAppender = new FileAppender();
		fileAppender.setName(fileName);
		fileAppender.setFile(ConfigProps.getProperty(Constants.LOGS_PATH) + System.getProperty("file.separator") 
				+ jobName + System.getProperty("file.separator") + fileName + ".log");
		fileAppender.setLayout(new PatternLayout("%d,%m%n"));
		fileAppender.setThreshold(Level.DEBUG);
		fileAppender.setAppend(true);
		fileAppender.activateOptions();
		return fileAppender;
	}
	
	@Override
	public void info(Object msg) {
		logger.info(msg);
	}
	
	@Override
	public void error(Object msg) {
		logger.error(msg);
	}

}
