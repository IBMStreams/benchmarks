// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
//  
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import com.ibm.streamsx.storm.email.benchmark.avro.Email;

import backtype.storm.tuple.Tuple;

public class Serialization {
	
	private DatumWriter<Email> emailDatumWriter;
	private ByteArrayOutputStream outputStream;
	private Encoder encoder;
	private byte[] serializedBytes;
	
	public Serialization() {
		emailDatumWriter = new SpecificDatumWriter<Email>(Email.class);
		outputStream = new ByteArrayOutputStream();		
	}
	
	public byte[] avroSerialize(Email email) throws IOException {
		encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        emailDatumWriter.write(email, encoder);
		encoder.flush();
		serializedBytes = outputStream.toByteArray();
        outputStream.reset();
		return serializedBytes;
	}
	
	public byte[] avroSerialize(String ID, String from, String date, 
			String subject, String toList, String ccList, String bccList, String body,
			int charCount, int wordCount, int paraCount) throws IOException {
		return avroSerialize(new Email(ID, from, date, subject, toList, ccList, 
				bccList, body, charCount, wordCount, paraCount));
	}
	
	public byte[] avroSerialize(Tuple input) throws IOException {
		return avroSerialize(input.getString(0), input.getString(1),
				input.getString(2), input.getString(3), input.getString(4),
				input.getString(5), input.getString(6), input.getString(7),
				input.getInteger(8), input.getInteger(9), input.getInteger(10));
	}
	
	public void close() throws IOException {
		outputStream.close();
	}
	
}
