// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
//  
package com.ibm.streamsx.storm.email.benchmark.utils;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import com.ibm.streamsx.storm.email.benchmark.avro.Email;

import backtype.storm.tuple.Tuple;

public class Deserialization {
	
	private DatumReader<Email> emailDatumReader;
	private Email readEmail;
	private Decoder decoder;
	
	public Deserialization() {
		emailDatumReader = new SpecificDatumReader<Email>(Email.class);
		readEmail = null;
	}
	
	public Email avroDeserialize(File file) throws IOException {
		DataFileReader<Email> dataFileReader = new DataFileReader<Email>(file, emailDatumReader);
		return dataFileReader.next();
	}
	
	public Email avroDeserialize(byte[] serialized) throws IOException {		
        decoder = DecoderFactory.get().binaryDecoder(serialized, null);
        readEmail = emailDatumReader.read(readEmail, decoder);
        return readEmail;
	}
	
	public Email avroDeserialize(Tuple input) throws IOException {
		return avroDeserialize(input.getBinary(0));
	}
	
}