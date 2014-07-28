// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.io.IOException;
import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.avro.Email;
import com.ibm.streamsx.storm.email.benchmark.utils.Deserialization;
import com.ibm.streamsx.storm.email.benchmark.utils.Misc;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


@SuppressWarnings("serial")
public class AvroDeserializeBolt extends BaseRichBolt {
	
	OutputCollector _collector;
	private Deserialization deserialization;
	private Email readEmail;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		deserialization = new Deserialization();
	}

	@Override
	public void execute(Tuple input) {		
        try {
			readEmail = deserialization.avroDeserialize(input);
				_collector.emit(new Values(readEmail.getID().toString(), 
						readEmail.getFrom().toString(), readEmail.getDate().toString(), 
						readEmail.getSubject().toString(), readEmail.getToList().toString(),
						readEmail.getCcList().toString(), readEmail.getBccList().toString(), 
						readEmail.getBody().toString(), readEmail.getCharCount(), 
						readEmail.getWordcount(), readEmail.getParaCount()));
				_collector.ack(input);
		} catch (IOException e) {
			_collector.fail(input);
			e.printStackTrace();
		}
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(Misc.getEmailFields());
    }

}  
