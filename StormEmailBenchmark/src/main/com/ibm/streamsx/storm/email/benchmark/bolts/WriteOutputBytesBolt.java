// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.io.IOException;
import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;
import com.ibm.streamsx.storm.email.benchmark.utils.CustomLogger;
import com.ibm.streamsx.storm.email.benchmark.utils.Misc;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;


@SuppressWarnings("serial")
public class WriteOutputBytesBolt extends BaseRichBolt {
	
	private static CustomLogger LOG;
	private boolean logMode;
	private OutputCollector _collector;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		try {
			logMode = ConfigProps.getBooleanProperty(Constants.IO_LOG_MODE);
			if(logMode) {
				LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), "WriteOutputBytesBolt");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(Tuple input) {
		if(input.getSourceComponent().equals("serialize")) {
			try {
		        Misc.writeToDisk(input.getBinary(0));
		        if(logMode)
		        	LOG.info("email");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		_collector.ack(input);
     
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}  
