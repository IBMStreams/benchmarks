// *******************************************************************************
// * Copyright (C)2014, International Business Machines Corporation and          *
// * others. All Rights Reserved.                                                *
// *******************************************************************************
// 
package com.ibm.streamsx.storm.email.benchmark.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringEscapeUtils;

import com.ibm.streamsx.storm.email.benchmark.avro.Email;

public class CoalesceEnronDataset {
	
	private File directory;
	private int fileCount;
	private File outputFile;
	private File outputFileLog = null;
	private BufferedWriter outputLog = null;
	private PrintWriter pw;
	private final int emailCount25 = 129356;
	private boolean dataset25 = false;
	
	public CoalesceEnronDataset(String path, String outputPath, String dataset25) {
		directory = new File(path);
		fileCount = 0;
		outputFile = new File(outputPath);
		if(dataset25.equals("yes")) {
			this.dataset25 = true;
		}
	}
	
	public CoalesceEnronDataset(String path, String outputPath, String dataset25, String outputFileLog) {
		this(path, outputPath, dataset25);
		this.outputFileLog = new File(outputFileLog);
	}

	public void create() {
		try {
			pw = new PrintWriter(outputFile);
			if(outputFileLog != null) {
				outputLog = new BufferedWriter(new FileWriter(outputFileLog));
			}
			walkDirectory(directory);
			System.out.println("Total emails processed: " + fileCount);						
		} catch(RuntimeException e) {
			System.out.println("Done with 25% of the file");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(outputFileLog != null) {
				try {
					outputLog.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			pw.close();
		}
		
	}
	
	private static String clean(CharSequence body) {
		return StringEscapeUtils.escapeJava(body.toString());
	}

	private void walkDirectory(File directory) throws IOException, MessagingException {	
		System.out.println("Diving into directory " + directory.getAbsolutePath());
		System.out.println("Emails processed: " + fileCount);
	    for(File file: directory.listFiles()) {
	        if (file.isDirectory()) {
	            walkDirectory(file);
	        } else {	        		        	
	            fileCount++;
	            Email email = convertToAvro(file);
	            pw.print("{ID=\"" + email.getID() + "\"");
	            pw.print(", From=\"" + clean(email.getFrom()) + "\"");
	            pw.print(", Date=\"" + email.getDate() + "\"");
	            pw.print(", Subject=\"" + clean(email.getSubject()) + "\"");
	            pw.print(", ToList=\"" + clean(email.getToList()) + "\"");
	            pw.print(", CcList=\"" + clean(email.getCcList()) + "\"");
	            pw.print(", BccList=\"" + clean(email.getBccList()) + "\"");
	            pw.print(", Body=\"" + clean(email.getBody()) + "\"");
	            pw.println("}");
	            
	            if(outputFileLog != null) {
	            	outputLog.write(email.getID() + "\n");
	            }
	            
	            if(dataset25) {
	            	if(fileCount >= emailCount25) {
	            		throw new RuntimeException("Done with file");
	            	}
	            }
	        }
	    }	 	    
	}
	
	private Email convertToAvro(File file) throws IOException, MessagingException {
		Email email = new Email();
		InputStream is = new FileInputStream(file);
		Properties props = new Properties();
		props.setProperty("mail.mime.address.strict", "false");
		Session sess = Session.getDefaultInstance(props);		 
		MimeMessage message = new MimeMessage(sess, is);
		email.setID(message.getMessageID());
		email.setDate(message.getSentDate().toString());
		email.setFrom(message.getFrom()[0].toString());
		email.setToList(getRecipientList(message, RecipientType.TO));
		email.setCcList(getRecipientList(message, RecipientType.CC));
		email.setBccList(getRecipientList(message, RecipientType.BCC));
		email.setSubject(message.getSubject());
		email.setBody(message.getContent().toString());				
		is.close();		  
		return email;
	}
	
	private String getRecipientList(MimeMessage message, RecipientType type) throws MessagingException {
		StringBuffer toList = new StringBuffer();
		Address[] addresses =  message.getRecipients(type);
		if(addresses != null) {
			for(int i = 0; i < addresses.length; i++) {
				toList.append(addresses[i].toString());
				if(i != addresses.length - 1) {
					toList.append(", ");
				}
			}
		} 
		return toList.toString();
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length == 3) {
			CoalesceEnronDataset cD = new CoalesceEnronDataset(args[0], args[1], args[2]);
			cD.create();
		} else if(args.length == 4) {
			CoalesceEnronDataset cD = new CoalesceEnronDataset(args[0], args[1], args[2], args[3]);
			cD.create();
		}
		else {
			System.out.println("Usage: <input_path> <output_file_path>"
					+ " <25%_dataset_yes_or_no> <optional: output_file_id_path>");
		}
	}	
}
