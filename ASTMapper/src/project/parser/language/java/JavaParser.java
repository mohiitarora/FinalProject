package project.parser.language.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import jd.core.Decompiler;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import project.parser.language.api.IParserExecution;
import project.parser.language.api.ParserUtils;

import com.boa.ast.framework.ASTRoot;
import com.main.runner.Constants;
import com.main.runner.MainRunner;

public class JavaParser implements IParserExecution{

	private JavaASTMapper mapper = new JavaASTMapper();
	private ASTRoot boaASTRoot;
	
	@Override
	public void parse() throws Exception {
		startParsing();
	}

	public void mapASTTypes(CompilationUnit cu,AST ast) throws Exception {
		 boaASTRoot = mapper.mapJavaAstToBoaAst(cu,ast);
		 System.out.println(boaASTRoot.toString());
	}
	
	public void startParsing() throws Exception{
		
		// Get the list of all the projects in language directory
		ArrayList<String> projects = ParserUtils.filesInDirectory(Constants.projectLocation+MainRunner.languageProcessed+"/");
		
		for(String projectName : projects){
			
			if(projectName.contains("jar")){
	            int numDecompiled = new Decompiler().decompileToDir(Constants.projectLocation+MainRunner.languageProcessed+"/"+projectName, Constants.tempWorkArea);
	            System.out.println("Decompiled " + numDecompiled + " classes");
			}else{
				//Extract the contents of Project in Temp Area.
				ParserUtils.unzipJar(Constants.projectLocation+MainRunner.languageProcessed+"/"+projectName);
			}
			
			// Get the list of all the extracted files from Temp directory
			Collection<File> unzippedFiles = ParserUtils.getRecursiveFileList(Constants.tempWorkArea);
			CompilationUnit cu;
			
			for(File file : unzippedFiles){
				if(file.getName().endsWith(".java")){
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					parser.setSource(ParserUtils.readFile(file).toCharArray());
					parser.setKind(ASTParser.K_COMPILATION_UNIT);
					parser.setResolveBindings(true);
					parser.setBindingsRecovery(true);
					cu = (CompilationUnit) parser.createAST(null);
					mapASTTypes(cu,cu.getAST());
				}
			}
			// Clean up the TEMP directory
			FileUtils.cleanDirectory(new File(Constants.tempWorkArea)); 
		}
	}

	public ASTRoot getBoaASTRoot() {
		return boaASTRoot;
	}

	public void setBoaASTRoot(ASTRoot boaASTRoot) {
		this.boaASTRoot = boaASTRoot;
	}
}
