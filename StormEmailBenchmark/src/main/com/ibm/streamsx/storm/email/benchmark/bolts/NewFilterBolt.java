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
import com.ibm.streamsx.storm.email.benchmark.utils.Filtering;
import com.ibm.streamsx.storm.email.benchmark.utils.Misc;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


@SuppressWarnings("serial")
public class NewFilterBolt extends BaseRichBolt {
	
	private OutputCollector _collector;
	private boolean logMode;
	private static CustomLogger LOG;
	private String filterToString;
	private String filterCCString;
	private String filterBCCString;
	private String body;
	private Filtering filtering;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		filtering = new Filtering();
		_collector = collector;
		try {
			logMode = ConfigProps.getBooleanProperty(Constants.FILTER_LOG_MODE);
			if(logMode) {
				LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
						"FilterBolt");
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void execute(Tuple input) {	
		if(filtering.checkFromEnron(input.getString(1))) {
			filterToString = filtering.removeEnronEmailBuilder(input.getString(4));
			filterCCString = filtering.removeEnronEmailBuilder(input.getString(5));
			filterBCCString = filtering.removeEnronEmailBuilder(input.getString(6));
			
			body = filtering.removeJunk(input.getString(7));
			
			_collector.emit(new Values(input.getString(0), input.getString(1), 
					input.getString(2), input.getString(3), 
					filterToString, filterCCString, filterBCCString, 
					body,	input.getInteger(8), input.getInteger(9), 
					input.getInteger(10)));
			_collector.ack(input);
		} else {
			if(logMode)
				LOG.info(input.getString(0) + ", " + input.getString(1));
		}
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declare(Misc.getEmailFields());
    }

}  
