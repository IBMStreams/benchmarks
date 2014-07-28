// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.Misc;
import com.ibm.streamsx.storm.email.benchmark.utils.Modification;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


@SuppressWarnings("serial")
public class ModifyBolt extends BaseRichBolt {
	
	private OutputCollector _collector;
	private final String[] originalNames = {"Jeffrey Skilling", "Kenneth Lay", "Andrew Fastow"};
	private final String[] aliases = {"Person 1", "Person 2", "Person 3"};
	private String body;
	private int index;
	private String finalBody;
	private String maxWord;
	private Modification modification;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		modification = new Modification();
	}

	@Override
	public void execute(Tuple input) {
		body = input.getString(7);
		body = Modification.replaceNames(body, originalNames, aliases);
		finalBody = new String(body);
				
        if(body.length() > 100000) {
        	index = body.indexOf("base64");
        	if(index > -1) {
            	body = body.substring(0, index);
            } 
        }
		
		maxWord = modification.getMostFrequentWord(body);
		_collector.emit(new Values(input.getString(0), input.getString(1), 
				input.getString(2), maxWord + " " + input.getString(3), 
				input.getString(4), input.getString(5), input.getString(6), 
				finalBody, input.getInteger(8), input.getInteger(9), 
				input.getInteger(10)));
		_collector.ack(input);
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declare(Misc.getEmailFields());
    }

}  
