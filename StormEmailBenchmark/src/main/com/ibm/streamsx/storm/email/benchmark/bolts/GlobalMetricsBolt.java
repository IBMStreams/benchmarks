// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.bolts;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;
import com.ibm.streamsx.storm.email.benchmark.utils.CustomLogger;
import com.ibm.streamsx.storm.email.benchmark.utils.MetricCalculation;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;


@SuppressWarnings("serial")
public class GlobalMetricsBolt extends BaseRichBolt {
	
	private class LogTransaction {
		
		private int quorum;
		private int numEmails;
		private long charCount;
		private int wordCount;
		private int paraCount;
		private double throughput;
		
		public LogTransaction(int numEmails, int charCount, int wordCount, int paraCount, double throughput) {
			quorum = 1;
			this.numEmails = numEmails;
			this.charCount = charCount;
			this.wordCount = wordCount; 
			this.paraCount = paraCount;
			this.throughput = throughput;
		}

		public boolean checkQuorum(int requiredQuorum) {
			if(this.quorum != requiredQuorum) {
				return false;
			}
			return true;
		}
		
		public void updateCount(int numEmails, int charCount, int wordCount, int paraCount, double throughput) {
			this.numEmails += numEmails;
			this.charCount += charCount;
			this.wordCount += wordCount;
			this.paraCount += paraCount;
			this.throughput += throughput;
			this.quorum++;
		}
		
		public String getCounts() {
			return numEmails + "," + charCount
			+ "," + wordCount + "," + paraCount;
		}
		
		public double getThroughput() {
			return throughput / requiredQuorum;
		}
		
	}
	
	private class FinalMetrics implements Serializable {
		
		protected int numEmails;
		protected long charCount;
		protected int wordCount;
		protected int paraCount;
		protected long startTime;
		
		public FinalMetrics(long startTime) {
			this.numEmails = 0;
			this.charCount = 0;
			this.wordCount = 0; 
			this.paraCount = 0;
			this.startTime = startTime;
		}
		
		public void updateCount(int charCount, int wordCount, int paraCount) {
			this.numEmails++;
			this.charCount += charCount;
			this.wordCount += wordCount;
			this.paraCount += paraCount;
		}
		
		private double getThroughput(double timeTaken) {
			return this.numEmails / timeTaken;
		}
		
		public String toString() {
			double timeTaken = MetricCalculation.msToSeconds(System.currentTimeMillis()- startTime);
			return  "\nCharacters: " + this.charCount + "\n" 
					+ "Words: " + this.wordCount + "\n"
					+ "Paragraphs: " + this.paraCount + "\n"
					+ "Emails processed: " + this.numEmails + "\n" 
					+ "Elapsed time (seconds): " + timeTaken + "\n"
					+ "Emails/s: " + this.getThroughput(timeTaken) + "\n";
		}
	}
	
	private HashMap<Integer, LogTransaction> pendingTransactions;
	private int requiredQuorum;
	private static CustomLogger LOG;
	private static CustomLogger LOG_THRU;
	private static CustomLogger LOG_FINAL;
	private FinalMetrics finalMetrics;
	private OutputCollector _collector;
	private int expectedCount = 0;
	
	public GlobalMetricsBolt(int requiredQuorum, long startTime) {
		this.requiredQuorum = requiredQuorum;
		this.finalMetrics = new FinalMetrics(startTime);
	}
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		pendingTransactions = new HashMap<Integer, LogTransaction>();
		try {
			LOG = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
					"GlobalMetricsBolt");
			LOG_THRU = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
					"GlobalMetricsBolt_Throughput");
			LOG_FINAL = CustomLogger.getLogger(conf.get(Config.TOPOLOGY_NAME).toString(), 
					"GlobalMetricsBolt_Final");
			this.expectedCount = ConfigProps.getIntProperty(Constants.TOTAL_EMAILS) 
					* ConfigProps.getIntProperty(Constants.SPOUT_INPUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void execute(Tuple input) {
		if (input.getSourceStreamId().equals(Constants.STREAM_FINALOFFPATH)) {
			this.finalMetrics.updateCount(input.getInteger(0), input.getInteger(1), 
				input.getInteger(2));
		} else {
		
			int id = input.getInteger(0);
			
			if(pendingTransactions.containsKey(id)) {
				LogTransaction transaction = pendingTransactions.get(id);
				transaction.updateCount(input.getInteger(1), input.getInteger(2), 
						input.getInteger(3), input.getInteger(4), input.getDouble(5));
				if (transaction.checkQuorum(this.requiredQuorum)) {
					LOG.info(transaction.getCounts());
		    		LOG_THRU.info(transaction.getThroughput());
				} else {
					pendingTransactions.put(id, transaction);
				}
			} else {
				LogTransaction transaction = new LogTransaction(input.getInteger(1), input.getInteger(2), 
						input.getInteger(3), input.getInteger(4), input.getDouble(5));
				pendingTransactions.put(id, transaction);
				if (transaction.checkQuorum(this.requiredQuorum)) {
					LOG.info(transaction.getCounts());
		    		LOG_THRU.info(transaction.getThroughput());
				} 
			}
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
