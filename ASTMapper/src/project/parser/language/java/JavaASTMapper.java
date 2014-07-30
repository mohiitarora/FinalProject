package project.parser.language.java;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.boa.ast.framework.ASTRoot;
import com.boa.ast.framework.Decleration;
import com.boa.ast.framework.Expression;
import com.boa.ast.framework.ExpressionKind;
import com.boa.ast.framework.Method;
import com.boa.ast.framework.Modifier;
import com.boa.ast.framework.ModifierKind;
import com.boa.ast.framework.Namespace;
import com.boa.ast.framework.Statement;
import com.boa.ast.framework.StatementKind;
import com.boa.ast.framework.Type;
import com.boa.ast.framework.TypeKind;
import com.boa.ast.framework.Variable;
import com.boa.ast.framework.Visibility;

public class JavaASTMapper {

	public ASTRoot boaASTRoot = null;
	
	public ASTRoot mapJavaAstToBoaAst(CompilationUnit cu, AST ast){
		boaASTRoot = new ASTRoot();
		addImports(boaASTRoot,cu);
		addNameSpaces(boaASTRoot,cu);		
		
		return boaASTRoot;
	}

	private void addNameSpaces(ASTRoot boaASTRoot2, CompilationUnit cu) {
		ArrayList<Namespace> namespaces = new ArrayList<Namespace>();
		Namespace namespace = new Namespace();
		namespace.setName((cu.getPackage()==null)?"":cu.getPackage().toString());
		addNamespaceDeclerations(namespace,cu);
		namespaces.add(namespace);
		boaASTRoot2.setNamespaces(namespaces);
	}
	
	private void addNamespaceDeclerations(Namespace namespace,CompilationUnit cu){
		ArrayList<Decleration> declerations = new ArrayList<Decleration>();
		
		final Decleration decleration = new Decleration();
		
		cu.accept(new ASTVisitor() {
			public boolean visit(TypeDeclaration typeNote) {
				decleration.setName(typeNote.getName().toString());
				decleration.setKind(typeNote.isInterface()?TypeKind.INTERFACE:TypeKind.CLASS);
				return true;
			}
		});
		
		addVariablesFieldsToDecleration(decleration,cu);
		addMethodsToDecleration(decleration,cu);
		
		declerations.add(decleration);
		namespace.setDeclerations(declerations);
	}

	private void addVariablesFieldsToDecleration(Decleration decleration,CompilationUnit cu) {
		final ArrayList<Variable> fields = new ArrayList<Variable>();

		cu.accept(new ASTVisitor() {
			@Override
            public boolean visit(final VariableDeclarationFragment node) {
				if(node.getParent() instanceof FieldDeclaration){
	            	Variable variable = new Variable();
	            	variable.setName(node.getName()!=null?node.getName().toString():"");
	            	Type type = new Type();
	            	type.setName(((FieldDeclaration)node.getParent()).getType().toString());
	            	type.setType(TypeKind.OTHER);
	            	variable.setVariable_type(type);
	            	Expression expression = new Expression();
	            	expression.setLiteral(node.getInitializer()!=null?node.getInitializer().toString():"");
	            	expression.setKind(ExpressionKind.VARDECL);
	            	variable.setInitializer(expression);
	            	
	            	FieldDeclaration fdec = ((FieldDeclaration)node.getParent());	            	
	            	ArrayList<Modifier> modifiers = new ArrayList<Modifier>();
	            	
	            	for(Object mf : fdec.modifiers()){
	            		ModifierKeyword s = ((org.eclipse.jdt.core.dom.Modifier)mf).getKeyword();
	            		Modifier modifier = new Modifier();
	            		evaluateModfierKind(s.toString(),modifier);
		            	modifiers.add(modifier);
	            	}
	            	variable.setModifiers(modifiers);
	                fields.add(variable);
				}
                return true;
            }
        });
		decleration.setFields(fields);
	}
	
	private void evaluateModfierKind(String modifierType,Modifier modifier) {
		if(modifierType.equals("static")){
			modifier.setKind(ModifierKind.STATIC);
		}else if(modifierType.equals("final")){
			modifier.setKind(ModifierKind.FINAL);
		}else if(modifierType.equals("public")){
			modifier.setVisibility(Visibility.PUBLIC);
		}else if(modifierType.equals("protected")){
			modifier.setVisibility(Visibility.PROTECTED);
		}else if(modifierType.equals("private")){
			modifier.setVisibility(Visibility.PRIVATE);
		}
	}
	
	private void addMethodsToDecleration(Decleration decleration,final CompilationUnit cu) {
		final ArrayList<Method> methods = new ArrayList<Method>();
		
		cu.accept(new ASTVisitor() {
			Method method = new Method();
			 
            @Override
            public boolean visit(final MethodDeclaration node) {
            	
            	System.out.println("METHOD: "+node.getName().toString());
            	method.setName(node.getName().toString());
            	Type typeVal = new Type();
            	typeVal.setName(node.getReturnType2()!=null?node.getReturnType2().toString():"");
            	method.setReturn_type(typeVal);
            	
            	// METHOD - GENERIC PARAMETERS
            	ArrayList<Type> generic_parameters = new ArrayList<Type>();
                for (Object parameter : node.parameters()) {
                    VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;
                    String type = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString();
                    Type typeData = new Type();
                    typeData.setName(type);
                    typeData.setType(TypeKind.OTHER);
                    generic_parameters.add(typeData);
                }
                
                final ArrayList<Modifier> modifiers = new ArrayList<Modifier>();
                node.accept(new ASTVisitor() {
                	 @Override
                     public boolean visit(final org.eclipse.jdt.core.dom.Modifier node) {
                		 Modifier modifier = new Modifier();
                		 evaluateModfierKind(node.getKeyword().toString(),modifier);
                		 modifiers.add(modifier);
						 return true;
                	 }
                });
                
                
            	final ArrayList<Statement> statements = new ArrayList<Statement>();
            	final ArrayList<Variable> fields = new ArrayList<Variable>();
            	node.accept(new ASTVisitor() {
            			  
            		 @Override
	                 public boolean visit(final VariableDeclarationFragment node) {
     	            	Variable variable = new Variable();
    	            	variable.setName(node.getName()!=null?node.getName().toString():"");
    	            	Type type = new Type();
    	            	if(node.getParent() instanceof VariableDeclarationStatement){
        	            	type.setName(((VariableDeclarationStatement)node.getParent()).getType().toString());
    	            	}
    	            	type.setType(TypeKind.OTHER);
    	            	variable.setVariable_type(type);
    	            	Expression expression = new Expression();
    	            	expression.setLiteral(node.getInitializer()!=null?node.getInitializer().toString():"");
    	            	expression.setKind(ExpressionKind.VARDECL);
    	            	variable.setInitializer(expression);
    	            	fields.add(variable);
	               		 return true;
	               	 }
            		 
	               	 @Override
	                 public boolean visit(final IfStatement node) {
	 	            	 Statement statement = new Statement();
	               		 statement.setKind(StatementKind.IF);
	               		 Expression condition = new Expression();
	               		 condition.setLiteral(node.getExpression().toString());
	               		 condition.setKind(ExpressionKind.CONDITIONAL);
						 statement.setCondition(condition );
	               		 statements.add(statement);
	               		 return true;
	               	 }
	               	 
	               	@Override
	                public boolean visit(final WhileStatement node) {
	 	            	 Statement statement = new Statement();
	               		statement.setKind(StatementKind.WHILE);
	               		 Expression condition = new Expression();
	               		 condition.setLiteral(node.getExpression().toString());
	               		 condition.setKind(ExpressionKind.CONDITIONAL);
	               		statements.add(statement);
	               		return true;
	               	}
	               	
	              	@Override
	                public boolean visit(final ForStatement node) {
	 	            	 Statement statement = new Statement();
	               		statement.setKind(StatementKind.FOR);
	               		 Expression condition = new Expression();
	               		 condition.setLiteral(node.getExpression()!=null?node.getExpression().toString():"");
	               		 condition.setKind(ExpressionKind.CONDITIONAL);
	               		statements.add(statement);
	               		return true;
	               	}
	 
	              	@Override
	                public boolean visit(final DoStatement node) {
	 	            	 Statement statement = new Statement();
	               		statement.setKind(StatementKind.DO);
	               		 Expression condition = new Expression();
	               		 condition.setLiteral(node.getExpression().toString());
	               		 condition.setKind(ExpressionKind.CONDITIONAL);
	               		statements.add(statement);
	               		return true;
	               	}           	
               });
            	
            	method.setVariables(fields);
            	method.setStatements(statements);
                method.setModifiers(modifiers);
                method.setGeneric_parameters(generic_parameters);
                methods.add(method);
                return true;
            }
        });
		decleration.setMethods(methods);
	}

	private void addImports(ASTRoot boaASTRoot2, CompilationUnit cu) {
		ArrayList<String> cuImports = new ArrayList<String>();
		ListIterator<?> litr = cu.imports().listIterator();
	    while(litr.hasNext()) {
	    	cuImports.add(litr.next().toString());
	    }
		boaASTRoot2.setImports(cuImports);	
	}
}
