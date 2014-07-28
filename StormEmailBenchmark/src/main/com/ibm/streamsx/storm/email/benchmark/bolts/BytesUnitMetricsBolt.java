// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.Constants;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class BytesUnitMetricsBolt extends BaseRichBolt {
	
	private OutputCollector _collector;
	private byte[] email;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple input) {		
		email = input.getBinary(0);
		updateGlobalMetrics(email.length);

		_collector.emit(Constants.STREAM_PATH, new Values(email));
		_collector.ack(input);
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declareStream(Constants.STREAM_PATH, new Fields("email"));
    	declarer.declareStream(Constants.STREAM_FINALOFFPATH, 
    			new Fields("length"));
    }
    
    private void updateGlobalMetrics(int length) {
    	this._collector.emit(Constants.STREAM_FINALOFFPATH, new Values(length));
    }
    
}  
