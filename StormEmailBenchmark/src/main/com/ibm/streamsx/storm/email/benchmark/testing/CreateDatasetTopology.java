// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.testing;

import com.ibm.streamsx.storm.email.benchmark.bolts.AvroSerializeBolt;
import com.ibm.streamsx.storm.email.benchmark.bolts.WriteOutputCompressBolt;
import com.ibm.streamsx.storm.email.benchmark.spouts.ReadEmailsSpout;
import com.ibm.streamsx.storm.email.benchmark.utils.ConfigProps;
import com.ibm.streamsx.storm.email.benchmark.utils.Constants;

import backtype.storm.topology.TopologyBuilder;


public class CreateDatasetTopology {

    
    public static void main(String[] args) throws Exception {
    	
    	TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout("spout", new ReadEmailsSpout(), 
        		ConfigProps.getIntProperty(Constants.SPOUT_INPUT));  
        
        builder.setBolt("serialize", new AvroSerializeBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_SERIALIZE))
        .shuffleGrouping("spout"); 
        
        builder.setBolt("writeoutput", new WriteOutputCompressBolt(), 
        		ConfigProps.getIntProperty(Constants.BOLT_OUTPUT))
        .shuffleGrouping("serialize");

        ConfigProps.configureTopologyLocal(builder, args);
        
    }
}
