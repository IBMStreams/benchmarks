// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.IOException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public class LogbackLogger extends CustomLogger {
	
	private Logger logger;
	
	public LogbackLogger(String jobName, 
			String fileName) throws IOException {
		logger = (Logger) LoggerFactory.getLogger(fileName);
		logger.detachAndStopAllAppenders();
		logger.addAppender(getFileAppender(jobName, fileName, 
				logger.getLoggerContext()));
	}
	
	private FileAppender<ILoggingEvent> getFileAppender(String jobName, 
			String fileName, LoggerContext context) throws IOException {
		FileAppender<ILoggingEvent> fileAppender 
		= new FileAppender<ILoggingEvent>();
		fileAppender.setContext(context);
		fileAppender.setName(fileName);
		fileAppender.setFile(ConfigProps.getProperty(Constants.LOGS_PATH) 
				+ System.getProperty("file.separator") 
				+ jobName + System.getProperty("file.separator") + fileName + ".log");
		PatternLayout pl = new PatternLayout();
		pl.setPattern("%d,%m%n");
		pl.setContext(context);
		pl.start();
		fileAppender.setLayout(pl);
		fileAppender.setAppend(true);
		fileAppender.start();
		return fileAppender;
	}
	
	@Override
	public void info(Object msg) {
		logger.info("{}", msg);
	}
	
	@Override
	public void error(Object msg) {
		logger.error("{}", msg);
	}

}
