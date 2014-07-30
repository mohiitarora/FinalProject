package com.main.runner;
import project.parser.language.api.IParserExecution;
import project.parser.language.cpp.CPPParser;
import project.parser.language.csharp.CSharpParser;
import project.parser.language.java.JavaParser;


public class ParserExecutor implements Constants{

	private String language;
	
	public ParserExecutor(String language){
		this.language = language;
	}
	 
	/**
	 * Method works on factory pattern and returns
	 * the Object for language executor.
	 * @return
	 */
	public IParserExecution parserHandler(){
		if(JAVA.equals(language)){
			return new JavaParser();
		}else if(CPP.equals(language)){
			return new CPPParser();
		}else if(CSHARP.equals(language)){
			return new CSharpParser();
		}
		return null;
	}
	
}
