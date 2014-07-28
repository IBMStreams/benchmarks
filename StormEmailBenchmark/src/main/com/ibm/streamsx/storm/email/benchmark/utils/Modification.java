// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class Modification {
	
	final static String wordDelimiter = "[^a-zA-Z0-9]";
	private Map<String, Integer> wordCount;
	private String[] words;
	private int maxCount;
	private String maxWord;
	
	public Modification() {
		wordCount = new HashMap<String, Integer>();
	}
	
	public static String replaceNames(String text, String[] originalNames, String[] aliases) {
		return StringUtils.replaceEach(text, originalNames, aliases);
	}
	
	public String getMostFrequentWord(String text) {
		wordCount.clear();
		words = text.split(wordDelimiter);
		
		for(String word: words) {
			if(word.length() >= 4 && word.length() <= 15) {
				word = word.toLowerCase();
				if (wordCount.containsKey(word)) {
					wordCount.put(word, wordCount.get(word) + 1);
				}
				else {
					wordCount.put(word, 1);
				}
			}
		}
		maxCount = 0;
		maxWord = null;
		
		for(Entry<String, Integer> pair: wordCount.entrySet()) {
			if(pair.getValue() > maxCount) {
				maxCount = pair.getValue();
				maxWord = pair.getKey();
			}
		}
		return maxWord;
	}
	
}