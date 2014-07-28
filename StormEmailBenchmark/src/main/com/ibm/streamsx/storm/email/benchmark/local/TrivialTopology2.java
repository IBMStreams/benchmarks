// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.local;

import com.ibm.streamsx.storm.email.benchmark.bolts.BytesGlobalMetricsBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.BytesSinkBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.BytesUnitMetricsBolt;
import com.ibm.streamsx.storm.email.benchmark.spouts.ReadEmailsDecompressSpout;
import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;

import backtype.storm.topology.TopologyBuilder;


public class TrivialTopology2 {

    public static void main(String[] args) throws Exception {
    	
    	TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout(Constants.SPOUT_INPUT, new ReadEmailsDecompressSpout(), 
        		ConfigProps.getIntProperty(Constants.SPOUT_INPUT));

        builder.setBolt(Constants.BOLT_METRICS, new BytesUnitMetricsBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_METRICS))
        		.localOrShuffleGrouping(Constants.SPOUT_INPUT, Constants.STREAM_PATH);
        
        builder.setBolt(Constants.BOLT_METRICS_GLOBAL, new BytesGlobalMetricsBolt(
        		ConfigProps.getIntProperty(Constants.BOLT_METRICS),
        		System.currentTimeMillis()), 
        		ConfigProps.getIntProperty(Constants.BOLT_METRICS_GLOBAL))
        		.globalGrouping(Constants.BOLT_METRICS, Constants.STREAM_FINALOFFPATH);
        
        builder.setBolt(Constants.BOLT_OUTPUT, new BytesSinkBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_OUTPUT))
        		.localOrShuffleGrouping(Constants.BOLT_METRICS, Constants.STREAM_PATH);

        ConfigProps.configureTopologyLocal(builder, args);        
    }
}
