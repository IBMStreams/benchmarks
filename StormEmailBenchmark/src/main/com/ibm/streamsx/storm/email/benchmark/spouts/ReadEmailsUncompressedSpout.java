// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.spouts;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;
import com.ibm.streamsx.storm.email.benchmark.utils.CustomLogger;
import com.ibm.streamsx.storm.email.benchmark.utils.ReadEmailUncompressedFile;

@SuppressWarnings("serial")
public class ReadEmailsUncompressedSpout extends BaseRichSpout {
    private SpoutOutputCollector _collector;  
    private ReadEmailUncompressedFile pEF;   
    private static CustomLogger LOG;
    private int sleep;
    private boolean logMode;
	private byte[] readEmail;

    @Override
    public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        readEmail = null;        
	    try {
	    	String filename = ConfigProps.getProperty(Constants.FILE_PATH)
					+ File.separator + ConfigProps.getProperty(Constants.FILE_NAME);	    	
	    	if(ConfigProps.getBooleanProperty(Constants.SPECIFIC_FILE_MODE)) {
    			filename += context.getThisTaskIndex()
    					+ ConfigProps.getProperty(Constants.FILE_EXT);
    		}
    		else {
    			filename += ConfigProps.getProperty(Constants.FILE_EXT);
    		}
		    pEF = new ReadEmailUncompressedFile(filename);
			sleep = ConfigProps.getIntProperty(Constants.SPOUT_SLEEP);
		    logMode = ConfigProps.getBooleanProperty(Constants.IO_LOG_MODE);
		    if(logMode) {
				LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
						"ReadEmailsUncompressedSpout");
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void nextTuple() {
    	Utils.sleep(this.sleep);
		try {
			readEmail = pEF.getNextEmail();
			if(readEmail != null) {
				_collector.emit("path", new Values(readEmail));
				if(logMode)
					LOG.info(readEmail[0]);			
			} 
		} catch (Exception e) {			
			throw new RuntimeException(e);
		} 		
    }        

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declareStream(Constants.STREAM_PATH, new Fields("serialized"));
    }
     
}
