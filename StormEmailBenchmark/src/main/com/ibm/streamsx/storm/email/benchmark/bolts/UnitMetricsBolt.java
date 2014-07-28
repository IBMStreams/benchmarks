// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.Constants;
import com.ibm.streamsx.storm.email.benchmark.utils.Misc;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class UnitMetricsBolt extends BaseRichBolt {
	
	private OutputCollector _collector;
	private String body;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple input) {		
		body = input.getString(7);
		updateGlobalMetrics(body.length());

		_collector.emit(Constants.STREAM_PATH, new Values(input.getString(0), 
				input.getString(1), input.getString(2), input.getString(3), 
				input.getString(4), input.getString(5), input.getString(6), 
				body,	input.getInteger(8), input.getInteger(9), input.getInteger(10)));
		_collector.ack(input);
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declareStream(Constants.STREAM_PATH, Misc.getEmailFields());
    	declarer.declareStream(Constants.STREAM_FINALOFFPATH, new Fields("length"));
    }
    
    private void updateGlobalMetrics(int length) {
    	this._collector.emit(Constants.STREAM_FINALOFFPATH, new Values(length));
    }
    
}  
