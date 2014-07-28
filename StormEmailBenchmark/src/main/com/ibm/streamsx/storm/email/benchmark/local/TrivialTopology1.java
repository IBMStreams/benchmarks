// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.local;

import com.ibm.streamsx.storm.email.benchmark.bolts.AvroDeserializeBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.AvroSerializeBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.BytesGlobalMetricsBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.UnitMetricsBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.WriteOutputBytesBolt;
import com.ibm.streamsx.storm.email.benchmark.spouts.ReadEmailsDecompressSpout;
import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;

import backtype.storm.topology.TopologyBuilder;


public class TrivialTopology1 {

    public static void main(String[] args) throws Exception {
    	
    	TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout(Constants.SPOUT_INPUT, new ReadEmailsDecompressSpout(), 
        		ConfigProps.getIntProperty(Constants.SPOUT_INPUT));
        
        builder.setBolt(Constants.BOLT_DESERIALIZE, new AvroDeserializeBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_DESERIALIZE))
        		.localOrShuffleGrouping(Constants.SPOUT_INPUT, Constants.STREAM_PATH);

        builder.setBolt(Constants.BOLT_METRICS, new UnitMetricsBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_METRICS))
        		.localOrShuffleGrouping(Constants.BOLT_DESERIALIZE);
        
        builder.setBolt(Constants.BOLT_METRICS_GLOBAL, new BytesGlobalMetricsBolt(
        		ConfigProps.getIntProperty(Constants.BOLT_METRICS),
        		System.currentTimeMillis()), 
        		ConfigProps.getIntProperty(Constants.BOLT_METRICS_GLOBAL))
        		.globalGrouping(Constants.BOLT_METRICS, Constants.STREAM_FINALOFFPATH);
        
        builder.setBolt(Constants.BOLT_SERIALIZE, new AvroSerializeBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_SERIALIZE))
        		.localOrShuffleGrouping(Constants.BOLT_METRICS, Constants.STREAM_PATH);
        
        builder.setBolt(Constants.BOLT_OUTPUT, new WriteOutputBytesBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_OUTPUT))
        		.localOrShuffleGrouping(Constants.BOLT_SERIALIZE);

        ConfigProps.configureTopologyLocal(builder, args);        
    }
}
