package com.main.runner;

import project.parser.language.api.IParserExecution;

public class MainRunner {

	private OutputWriter writer = null;
	private IParserExecution parserHandler;
	public static String languageProcessed = "";
	
	public static void main(String [] args) throws Exception{
		
		//Languaue processed to be part of Run time parameters
		languageProcessed = args[0].toString();

		// Initialize components to handle parsing and mapping
		MainRunner mainRunner = new MainRunner();
		mainRunner.initComponents();
		
		// Start parsing and mapping for language specified and log execution run
		OutputWriter.writeOutputToFile("Processing PARSER for language : "+languageProcessed);
		mainRunner.parserHandler.parse();
	}
	
	/**
	 * Initialize the Parser and Mapper components to be used
	 * for procesisng of the language projects/files.
	 * 
	 * @throws Exception
	 */
	public void initComponents() throws Exception{
		
		// Initialize the Log Writer
		writer = new OutputWriter();
		writer.init();
		
		// Parse the input project/Jar/War/zipped file and process the file.
		ParserExecutor parser = new ParserExecutor(languageProcessed);
		parserHandler = parser.parserHandler();
		
		if(null == parserHandler){
			OutputWriter.writeOutputToFile("No Language Found. Please provide a Language for Parsing.");
			System.exit(-1);
		}
	}
}
