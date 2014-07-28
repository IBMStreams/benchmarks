// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;
import com.ibm.streamsx.storm.email.benchmark.utils.CustomLogger;
import com.ibm.streamsx.storm.email.benchmark.utils.MetricCalculation;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class BytesGlobalMetricsBolt extends BaseRichBolt {
	
	private class FinalMetrics implements Serializable {
		
		protected int numEmails;
		protected long startTime;
		protected long length;
		
		public FinalMetrics(long startTime) {
			this.numEmails = 0;
			this.startTime = startTime;
		}
		
		public void updateCount(int length) {
			this.length += length;
			this.numEmails++;
		}
		
		private double getThroughput(double timeTaken) {
			return this.numEmails / timeTaken;
		}
		
		public String toString() {
			double timeTaken = MetricCalculation.msToSeconds(System.currentTimeMillis()- startTime);
			return  "Emails processed: " + this.numEmails + "\n" 
					+ "Total bytes: " + this.length + "\n"
					+ "Elapsed time (seconds): " + timeTaken + "\n"
					+ "Emails/s: " + this.getThroughput(timeTaken) + "\n";
		}
	}
	
	private static CustomLogger LOG_FINAL;
	private FinalMetrics finalMetrics;
	private OutputCollector _collector;
	private int expectedCount = 0;
	
	public BytesGlobalMetricsBolt(int requiredQuorum, long startTime) {
		this.finalMetrics = new FinalMetrics(startTime);
	}
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		try {
			LOG_FINAL = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), "GlobalMetricsBolt_Final");
			this.expectedCount = ConfigProps.getIntProperty(Constants.TOTAL_EMAILS) 
					* ConfigProps.getIntProperty(Constants.SPOUT_INPUT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void execute(Tuple input) {
		if (input.getSourceStreamId().equals(Constants.STREAM_FINALOFFPATH)) {
			this.finalMetrics.updateCount(input.getInteger(0));
		}
		_collector.ack(input);
		// if we know that we're done then just dump final metrics to disk
		if(this.finalMetrics.numEmails == this.expectedCount) {
			this.cleanup();
		}
		
	}
	
	@Override
	public void cleanup() {
		LOG_FINAL.info(this.finalMetrics);
	}
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

} 