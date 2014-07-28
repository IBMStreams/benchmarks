// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

public class Constants {
	// props
	public static String BOLT_DESERIALIZE = "deserializebolt";
	public static String BOLT_MODIFY = "modifybolt";
	public static String BOLT_FILTER = "filterbolt";
	public static String BOLT_METRICS = "metricsbolt";
	public static String BOLT_SERIALIZE = "serializebolt";
	public static String BOLT_OUTPUT = "writeoutputbolt";
	public static String SPOUT_INPUT = "readinputspout";
	public static String SPOUT_SLEEP = "spoutsleep";
	public static String BOLT_METRICS_GLOBAL = "globalmetricsbolt";
	public static String FLUSH_INTERVAL = "flushinterval";
	public static String PROPERTY_FILE = "storm.email.properties";
	public static String STORM_VERSION = "stormversion";
	public static String TOTAL_EMAILS = "totalemails";
	public static String NUMS_PROCS = "numprocesses";
	public static String FILE_PATH = "filepath";
	public static String FILE_NAME = "filename";
	public static String FILE_EXT = "fileext";
	public static String OUTPUT_PATH = "outputpath";
	public static String DEBUG_MODE = "debug";
	public static String SPECIFIC_FILE_MODE = "specificfile";
	public static String IO_LOG_MODE = "logemailreadwrite";
	public static String FILTER_LOG_MODE = "logfilter";
	public static String LOGS_PATH = "logspath";
	
	// path
	public static String STREAM_PATH = "path";
	public static String STREAM_OFFPATH = "offpath";
	public static String STREAM_FINALOFFPATH = "finaloffpath";
}

