// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.tests;

import java.io.IOException;

import com.ibm.streamsx.storm.email.benchmark.avro.Email;
import com.ibm.streamsx.storm.email.benchmark.utils.Deserialization;
import com.ibm.streamsx.storm.email.benchmark.utils.Serialization;

import junit.framework.TestCase;

public class SerializationTest extends TestCase {
	
	private Serialization serialization;
	private Deserialization deserialization;
	
	public SerializationTest(String name) {
		super(name);
		serialization = new Serialization();
		deserialization = new Deserialization();
	}
	
	public void testSerialization() {
		Email testEmail = new Email();
		testEmail.setBccList("bcc");
		testEmail.setBody("bodyyy");
		testEmail.setID("id");
		testEmail.setFrom("from");
		testEmail.setDate("date");
		testEmail.setToList("to");
		testEmail.setCharCount(10);
		testEmail.setCcList("cc");
		testEmail.setParaCount(100);
		testEmail.setSubject("subject");
		testEmail.setWordcount(1000);
		try {
			Email deserializedEmail 
			= deserialization.avroDeserialize(serialization.avroSerialize(testEmail));
			assertEquals(testEmail, deserializedEmail);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				serialization.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}