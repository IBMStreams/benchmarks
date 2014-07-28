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
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;
import com.ibm.streamsx.storm.email.benchmark.utils.CustomLogger;
import com.ibm.streamsx.storm.email.benchmark.utils.Misc;
import com.ibm.streamsx.storm.email.benchmark.utils.ParseEmailFile;

@SuppressWarnings("serial")
public class ReadEmailsSpout extends BaseRichSpout {
    private SpoutOutputCollector _collector;  
    private ParseEmailFile pEF;   
    private static CustomLogger LOG;
    private static CustomLogger LOG_ERROR;
    private int sleep;
    private boolean logMode;

    @Override
    public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
	    try {
	    	if(ConfigProps.getBooleanProperty(Constants.SPECIFIC_FILE_MODE)) {
    			pEF = new ParseEmailFile(ConfigProps.getProperty(Constants.FILE_PATH) 
                                        + File.separator
    					+ ConfigProps.getProperty(Constants.FILE_NAME) 
    					+ context.getThisTaskIndex()
    					+ ConfigProps.getProperty(Constants.FILE_EXT));
    		}
    		else {
    			pEF = new ParseEmailFile(ConfigProps.getProperty(Constants.FILE_PATH)
                                        + File.separator
    					+ ConfigProps.getProperty(Constants.FILE_NAME)
    					+ ConfigProps.getProperty(Constants.FILE_EXT));
    		}
			sleep = ConfigProps.getIntProperty(Constants.SPOUT_SLEEP);
	    	logMode = ConfigProps.getBooleanProperty(Constants.IO_LOG_MODE);
	    	if(logMode) {
				LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
						"ReadEmailsSpout");				
	    	}
	    	LOG_ERROR = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
					"ReadEmailsSpout_Errors");
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void nextTuple() {
    	Utils.sleep(this.sleep);
		String[] email = pEF.getNextEmail();
		if(email[0] != null && email[1] != null && email[2] != null && email[3] != null
				 && email[4] != null && email[5] != null && email[6] != null
				 && email[7] != null) {
			if(logMode) {
				LOG.info(email[0]);
			}
			_collector.emit(new Values(email[0], email[1], email[2], email[3], 
				email[4], email[5], email[6], email[7], 0, 0, 0));
		} else {
			LOG_ERROR.error("Finished processing file");
			try {
				synchronized(this) {
				    this.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
        declarer.declare(Misc.getEmailFields());
    }
     
}
