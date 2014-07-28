// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.utils;

public class MetricCalculation {
	
	public static int getCharCountOld(String text) {
		int count = 0;
		for(char c: text.toCharArray()) {
			if(c != ' ' && c != '\n')
				count++;
		}
		return count;
	}
	
	public static int getCharCount(String text) {
		return text.length();
	}
	
	public static int getWordCount(String text) {
		return text.split("\\s+|\n+").length;
	}
	
	public static int getParagraphCount(String text) {
		return text.split("\n+").length;
	}
	
	public static int[] getWordAndParagraphCount(String text) {
		int[] counts = new int[2];
		counts[0] = counts[1] = 1;
		//WC: counts[0], PC: counts[1]
		char prev = '\0';
		for(char c: text.toCharArray()) {
			if(Character.isSpaceChar(c) && !Character.isSpaceChar(prev)) {
				counts[0]++;
			}
			if(c == '\n' && prev != '\n') {
				counts[0]++;
				counts[1]++;
			}
			prev = c; 
		}
		return counts;
	}
	
	public static double msToSeconds(long ms) {
    	return ms / 1000.0;
    }
	
	public static int[] getWordAndParagraphCountOld(String text) {
		int[] counts = new int[2];
		counts[0] = counts[1] = 1;
		//WC: counts[0], PC: counts[1]
		for(char c: text.toCharArray()) {
			if(Character.isWhitespace(c))
				counts[0]++;
			if (c == '\n') {
				counts[1]++;
			}
		}
		return counts;
	}

}