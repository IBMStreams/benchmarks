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
import com.ibm.streamsx.storm.email.benchmark.utils.WriteToDiskCompressed;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;


@SuppressWarnings("serial")
public class WriteOutputCompressBolt extends BaseRichBolt {
	
	private static CustomLogger LOG;
	private boolean logMode;
	private WriteToDiskCompressed wTDC;
	private OutputCollector _collector;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {	
		_collector = collector;
		try {
			wTDC = new WriteToDiskCompressed();
			logMode = ConfigProps.getBooleanProperty(Constants.IO_LOG_MODE);
			if(logMode) {
				LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
						"WriteOutputCompressBolt");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(Tuple input) {
		try {
			wTDC.write(input.getBinary(0));
			_collector.ack(input);
	        if(logMode) {
	        	LOG.info(input.getString(1));
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
	
	@Override
	public void cleanup() {
		try {
			wTDC.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}  
