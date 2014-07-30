package project.parser.language.cpp;

import java.util.ArrayList;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompoundStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarationStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDoStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTExpressionStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTForStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionCallExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDefinition;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIfStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTUsingDirective;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTWhileStatement;

import com.boa.ast.framework.ASTRoot;
import com.boa.ast.framework.Decleration;
import com.boa.ast.framework.Expression;
import com.boa.ast.framework.ExpressionKind;
import com.boa.ast.framework.Method;
import com.boa.ast.framework.Namespace;
import com.boa.ast.framework.Statement;
import com.boa.ast.framework.StatementKind;
import com.boa.ast.framework.Type;
import com.boa.ast.framework.TypeKind;
import com.boa.ast.framework.Variable;

public class CPPASTMapper {

	public ASTRoot boaASTRoot = null;
	
	public ASTRoot mapCppAstToBoaAst(IASTTranslationUnit translationUnit) throws Exception{
		boaASTRoot = new ASTRoot();				    
		addImports(boaASTRoot,translationUnit);
		addNameSpaces(boaASTRoot,translationUnit);	
		return boaASTRoot;
	}

	private void addNameSpaces(ASTRoot boaASTRoot2,IASTTranslationUnit translationUnit) throws Exception{
		ArrayList<Namespace> namespaces = new ArrayList<Namespace>();
		Namespace namespace = new Namespace();
		
		IASTDeclaration[] declerations = translationUnit.getDeclarations();
		for(int i=0;i<declerations.length;i++){
			if(declerations[i] instanceof CPPASTUsingDirective){
				namespace.setName(((CPPASTUsingDirective)declerations[i]).getQualifiedName().toString());
			}
		}
		
		addNamespaceDeclerations(namespace,translationUnit);
		namespaces.add(namespace);
		boaASTRoot2.setNamespaces(namespaces);
	}

	private void addNamespaceDeclerations(Namespace namespace,IASTTranslationUnit translationUnit) {
		ArrayList<Decleration> declerations = new ArrayList<Decleration>();
		Decleration decleration = new Decleration();
		
		addMethodsToDecleration(decleration,translationUnit);

		declerations.add(decleration);
		namespace.setDeclerations(declerations);
	}

	private void addMethodsToDecleration(Decleration decleration,IASTTranslationUnit translationUnit) {
		final ArrayList<Method> methods = new ArrayList<Method>();

		IASTDeclaration[] declerations = translationUnit.getDeclarations();
		for(int i=0;i<declerations.length;i++){
			if(declerations[i] instanceof CPPASTFunctionDefinition){
				CPPASTFunctionDefinition function = (CPPASTFunctionDefinition) declerations[i];
				Method method = new Method();
				method.setName(function.getDeclarator().getName().toString());
				addMethodGenericParameters(function,method);
				addMethodStatements(function,method);
				methods.add(method);
			}
		}
		
		decleration.setMethods(methods);
	}

	private void addMethodStatements(CPPASTFunctionDefinition function,Method method) {
		ArrayList<Statement> statements = new ArrayList<Statement>();
		Statement statement = new  Statement();
		CPPASTCompoundStatement bodyStatements = (CPPASTCompoundStatement) function.getBody();
		IASTStatement[] funcStatements = bodyStatements.getStatements();
		
		for(IASTStatement sttVal : funcStatements){
			if(sttVal instanceof CPPASTIfStatement){
           		 statement.setKind(StatementKind.IF);
           		 Expression condition = new Expression();
           		 condition.setLiteral(((CPPASTIfStatement)sttVal).getConditionExpression()==null?"" :
           			((CPPASTIfStatement)sttVal).getConditionExpression().getRawSignature());
           		 condition.setKind(ExpressionKind.CONDITIONAL);
				 statement.setCondition(condition );
           		 statements.add(statement);
			}else if(sttVal instanceof CPPASTDeclarationStatement){
				Variable variable = new Variable();
				CPPASTSimpleDeclaration decStmt = (CPPASTSimpleDeclaration) ((CPPASTDeclarationStatement) sttVal).getDeclaration();
				variable.setName(decStmt.getDeclarators().length>0? decStmt.getDeclarators()[0].getName().getRawSignature():"");
				Type type = new Type();
				type.setName(decStmt.getDeclSpecifier().getRawSignature());
				type.setType(TypeKind.OTHER);
				variable.setVariable_type(type);	
          		statement.setKind(StatementKind.TYPEDECL);
				statement.setVariable_declaration(variable);
			}else if(sttVal instanceof CPPASTExpressionStatement){
				Expression expression = new Expression();
				expression.setLiteral(((CPPASTExpressionStatement)sttVal).getRawSignature());
				if(((CPPASTExpressionStatement)sttVal).getExpression() instanceof CPPASTFunctionCallExpression){
					expression.setKind(ExpressionKind.METHODCALL);
					expression.setMethod(((CPPASTFunctionCallExpression)((CPPASTExpressionStatement)sttVal).getExpression()).getFunctionNameExpression().getRawSignature());
				}
         		statement.setKind(StatementKind.TYPEDECL);
				statement.setExpression(expression );
			}else if(sttVal instanceof CPPASTWhileStatement){
          		 statement.setKind(StatementKind.WHILE);
          		 Expression condition = new Expression();
          		 condition.setLiteral(((CPPASTWhileStatement)sttVal).getRawSignature());
          		 condition.setKind(ExpressionKind.CONDITIONAL);
				 statement.setCondition(condition );
          		 statements.add(statement);
			}else if(sttVal instanceof CPPASTDoStatement){
          		 statement.setKind(StatementKind.DO);
          		 Expression condition = new Expression();
          		 condition.setLiteral(((CPPASTDoStatement)sttVal).getRawSignature());
          		 condition.setKind(ExpressionKind.CONDITIONAL);
				 statement.setCondition(condition );
          		 statements.add(statement);
			}else if(sttVal instanceof CPPASTForStatement){
          		 statement.setKind(StatementKind.FOR);
          		 Expression condition = new Expression();
          		 condition.setLiteral(((CPPASTForStatement)sttVal).getConditionExpression().getRawSignature());
          		 condition.setKind(ExpressionKind.CONDITIONAL);
				 statement.setCondition(condition );
          		 statements.add(statement);
			}
		}
		statements.add(statement);
		method.setStatements(statements);
	}

	private void addMethodGenericParameters(CPPASTFunctionDefinition function,Method method) {
		ArrayList<Type> generic_parameters = new ArrayList<Type>();
		
		CPPASTFunctionDeclarator functionDeclerator = (CPPASTFunctionDeclarator) function.getDeclarator();
		IASTParameterDeclaration[] funcParameters = functionDeclerator.getParameters();
		for(IASTParameterDeclaration param : funcParameters){
			Type type = new Type();
			type.setName(param.getRawSignature());
			type.setType(TypeKind.OTHER);
			generic_parameters.add(type);
		}
		
		method.setGeneric_parameters(generic_parameters);
	}

	private void addImports(ASTRoot boaASTRoot2,IASTTranslationUnit translationUnit) {
		ArrayList<String> cuImports = new ArrayList<String>();
		IASTPreprocessorIncludeStatement[] imports = translationUnit.getTranslationUnit().getIncludeDirectives();
		for(int i=0; i< imports.length;i++){
			cuImports.add(imports[i].toString());
		}
		boaASTRoot2.setImports(cuImports);	
	}
}
