package com.main.runner;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class OutputWriter {

	public static final String downloadLocation = "/Users/mohitarora/Downloads/DATA/LOGS/";
	public static final File file = new File(downloadLocation);
	public static BufferedOutputStream fileWriter = null;

	public void init() throws Exception{
		
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.mkdirs();
		}
		fileWriter = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath()+"/ParserRunner_Out.txt"),true));
	}

	public static void writeOutputToFile(String data) throws Exception{
		System.out.println("LOG: "+data);
		fileWriter.write((data+"\n").getBytes());
	}
	
}
