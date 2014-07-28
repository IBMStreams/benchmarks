// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class Filtering {
	
	final static String junk = "=2*0*";
	private String[] list;
	private ArrayList<String> filteredList;
	
	public Filtering() {
		filteredList = new ArrayList<String>();
	}
	
	public boolean checkFromEnron(String from) {
		if (from.substring(0, Math.min(50, from.length())).contains("enron.com")) {
			return true;
		}
		return false;
	}
	
	public String removeEnronEmail(String emailList) {   
		filteredList.clear();
    	list = emailList.split(", ");    	
    	for(String emailAddr: list) {
			if(emailAddr.endsWith("@enron.com")) {
				filteredList.add(emailAddr);
			}
		}
    	return StringUtils.join(filteredList, ", ");
    }
	
	public String removeEnronEmailNoList(String emailList) {  
		if(!emailList.isEmpty()) {
			String str = "";
			list = emailList.split(", ");
			if(list.length == 1)
				return emailList;
	    	for(String emailAddr: list) {
				if(emailAddr.endsWith("@enron.com")) {
					str += emailAddr + ", ";
				}
			}
	    	if(str.length() == 0)
				return emailList;
	    	return str.substring(0, str.length() - 2);
		}
		return emailList;
    }
	
	public String removeEnronEmailBuilder(String emailList) {  
		if(!emailList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			list = emailList.split(", ");
			if(list.length == 1)
				return emailList;
	    	for(String emailAddr: list) {
				if(emailAddr.endsWith("@enron.com")) {
					sb.append(emailAddr + ", ");
				}
			}
	    	if(sb.length() == 0)
				return emailList;
	    	return sb.substring(0, sb.length() - 2);
		}
		return emailList;
    }
	
	public String removeJunkOld(String text) {
		return text.replaceAll(junk, "");
	}
	
	public String removeJunk(String text) {
		return text.replaceAll("=20\n", "\n").replaceAll("=\n", "");		
	}
	
}