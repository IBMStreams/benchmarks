// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;
 
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
 
public class ConfigProps {
	
	public static Properties prop; 
	
	public ConfigProps() throws IOException {
		String path = System.getProperty("user.home") + System.getProperty("file.separator");
		InputStream propsStream = new FileInputStream(path + Constants.PROPERTY_FILE);
		prop = new Properties();
		prop.load(propsStream);
	}
	
	public synchronized static String getProperty(String property) throws IOException {
		new ConfigProps();
		return prop.getProperty(property);
	}
	
	public synchronized static int getIntProperty(String property) throws IOException {
		return Integer.parseInt(getProperty(property));
	}
	
	public synchronized static float getFloatProperty(String property) throws IOException {
		return Float.parseFloat(getProperty(property));
	}
	
	public synchronized static boolean getBooleanProperty(String property) throws IOException {
		String value = getProperty(property);
		if(value.equals("false")) {
			return false;
		}
		else {
			return true;
		}
	}

    public static void configureTopologyLocal(TopologyBuilder builder, String[] args) 
    		throws IOException, AlreadyAliveException, InvalidTopologyException, InterruptedException {
    	Config conf = new Config();
        conf.setDebug(getBooleanProperty(Constants.DEBUG_MODE));
        if(args != null && args.length == 2) {
        	conf.setNumWorkers(getIntProperty(Constants.NUMS_PROCS));
        	if(args[0].equals("local")) {
        		 LocalCluster cluster = new LocalCluster();
                 cluster.submitTopology(args[1], conf, builder.createTopology());
        	} else if(args[0].equals("remote")) {
        		StormSubmitter.submitTopology(args[1], conf, builder.createTopology());
        	} else {
        		System.out.println("Error! Specify local or remote cluster"); 
        	}             
        } else {        
        	System.out.println("Specify local/remote cluster and/or job name!"); 
        }
    }
}