package project.parser.language.cpp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.cdt.core.dom.ICodeReaderFactory;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.CodeReader;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.parser.scanner2.FileCodeReaderFactory;

import project.parser.language.api.IParserExecution;
import project.parser.language.api.ParserUtils;

import com.boa.ast.framework.ASTRoot;
import com.main.runner.Constants;
import com.main.runner.MainRunner;

public class CPPParser implements IParserExecution {
	
	private CPPASTMapper mapper = new CPPASTMapper();
	private ASTRoot boaASTRoot;
	
	@Override
	public void parse() throws Exception{
		startParsing();
	}

	public void mapASTTypes(IASTTranslationUnit translationUnit, String fileName) throws Exception {
		 boaASTRoot = mapper.mapCppAstToBoaAst(translationUnit);
		 System.out.println("FileName: "+fileName+"\n"+boaASTRoot.toString());
	}

	public void startParsing() throws Exception{

		ArrayList<String> projects = ParserUtils.filesInDirectory(Constants.projectLocation+MainRunner.languageProcessed+"/");

		for(String projectName : projects){			
			if(projectName.endsWith(".bz2")){
				//Uncompress Bz2 file
				ParserUtils.uncompressBz2File(Constants.projectLocation+MainRunner.languageProcessed+"/"+projectName);
				//Unzip tar file.
				ParserUtils.unTarProject(new File(Constants.projectLocation+MainRunner.languageProcessed+"/"+projectName.replace(".bz2", "")),new File(Constants.tempWorkArea));
			}
		}
		
		// Get the list of all the extracted files from Temp directory
		Collection<File> unzippedFiles = ParserUtils.getRecursiveFileList(Constants.tempWorkArea);

		for(File file : unzippedFiles){
			if(file.getName().endsWith(".cpp")){
				System.out.println("Processing Fie: "+file.getName());
				IParserLogService log = new DefaultLogService();
				CodeReader reader = new CodeReader(ParserUtils.readFile(file).toCharArray());
				Map definedSymbols = new HashMap();
				String[] includePaths = new String[0];
				IScannerInfo info = new ScannerInfo(definedSymbols,includePaths);
				ICodeReaderFactory readerFactory = FileCodeReaderFactory.getInstance();
				
				IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(reader, info, readerFactory,null, log);
				mapASTTypes(translationUnit,file.getName());
			}
		}
		// Clean up the TEMP directory
		FileUtils.cleanDirectory(new File(Constants.tempWorkArea)); 
	}
}
