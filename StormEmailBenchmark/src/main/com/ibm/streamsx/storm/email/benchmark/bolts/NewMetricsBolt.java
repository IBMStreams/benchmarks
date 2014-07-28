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
import com.ibm.streamsx.storm.email.benchmark.utils.MetricCalculation;
import com.ibm.streamsx.storm.email.benchmark.utils.Misc;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


@SuppressWarnings("serial")
public class NewMetricsBolt extends BaseRichBolt {
	
	private OutputCollector _collector;
	private int globalNumEmails;
	private int intervalNumEmails;
	private int intervalCC;
	private int intervalWC;
	private int intervalPC;
	private int flushInterval; //in seconds
	private int transactionID;
	private long lastFlushTime;
	private static CustomLogger LOG;
	private static CustomLogger LOG_THRU;
	private String body;
	private int charCount;
	private int wordCount;
	private int paraCount;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		globalNumEmails = 0;
		intervalNumEmails = 0;
		intervalCC = 0;
		intervalWC = 0;
		intervalPC = 0;
		transactionID = 0;
		try {
			LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
					"MetricsBolt");
			LOG_THRU = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
					"MetricsBolt_Throughput");
			flushInterval = ConfigProps.getIntProperty(Constants.FLUSH_INTERVAL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastFlushTime = System.currentTimeMillis();
	}

	@Override
	public void execute(Tuple input) {
		body = input.getString(7);
		
		charCount = MetricCalculation.getCharCount(body);
		int counts[] = MetricCalculation.getWordAndParagraphCount(body);
		wordCount = counts[0];
		paraCount = counts[1];
		
		updateGlobalMetrics(charCount, wordCount, paraCount);

		_collector.emit(Constants.STREAM_PATH, new Values(input.getString(0), 
				input.getString(1), input.getString(2), input.getString(3), 
				input.getString(4), input.getString(5), input.getString(6), 
				input.getString(7),	charCount, wordCount, paraCount));
		_collector.ack(input);
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declareStream(Constants.STREAM_PATH, Misc.getEmailFields());
    	declarer.declareStream(Constants.STREAM_OFFPATH, new Fields("id", "numemails", "charcount", 
    			"wordcount", "paracount", "throughput"));
    	declarer.declareStream(Constants.STREAM_FINALOFFPATH, new Fields("charcount", 
    			"wordcount", "paracount"));
    }
    
    private void updateGlobalMetrics(int CC, int WC, int PC) {
    	globalNumEmails++;
    	intervalNumEmails++;
    	intervalCC += CC;
    	intervalWC += WC;
    	intervalPC += PC;
    	long currentTime = System.currentTimeMillis();    	
    	if(MetricCalculation.msToSeconds(currentTime - lastFlushTime) > flushInterval) {
    		LOG.info(globalNumEmails + "," + intervalCC
    				+ "," + intervalWC + "," + intervalPC);
    		double throughput = ((double)intervalNumEmails) / flushInterval;
    		LOG_THRU.info(throughput);
    		transactionID++;
    		this._collector.emit(Constants.STREAM_OFFPATH, 
    				new Values(transactionID, globalNumEmails,
    				intervalCC, intervalWC, intervalPC, throughput));
    		intervalNumEmails = 0;
    		lastFlushTime = currentTime;
    		intervalCC = intervalWC = intervalPC = 0;
    	}
    	this._collector.emit(Constants.STREAM_FINALOFFPATH, new Values(CC, WC, PC));

    }
    
}  
