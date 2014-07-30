package com.boa.ast.framework;

import java.util.ArrayList;

public class Expression {

	private Modifier annotation;
	private Decleration anon_declaration;
	private ArrayList<Expression> expressions;
	private ArrayList<Type> generic_parameters;
	private boolean is_postfix;
	private ExpressionKind kind;
	private String literal;
	private String method;
	private ArrayList<Expression> method_args;
	private Type new_type;
	private String variable;
	private ArrayList<Variable> variable_decls;
	
	public Modifier getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Modifier annotation) {
		this.annotation = annotation;
	}
	public Decleration getAnon_declaration() {
		return anon_declaration;
	}
	public void setAnon_declaration(Decleration anon_declaration) {
		this.anon_declaration = anon_declaration;
	}
	public ArrayList<Expression> getExpressions() {
		return expressions;
	}
	public void setExpressions(ArrayList<Expression> expressions) {
		this.expressions = expressions;
	}
	public ArrayList<Type> getGeneric_parameters() {
		return generic_parameters;
	}
	public void setGeneric_parameters(ArrayList<Type> generic_parameters) {
		this.generic_parameters = generic_parameters;
	}
	public boolean isIs_postfix() {
		return is_postfix;
	}
	public void setIs_postfix(boolean is_postfix) {
		this.is_postfix = is_postfix;
	}
	public ExpressionKind getKind() {
		return kind;
	}
	public void setKind(ExpressionKind kind) {
		this.kind = kind;
	}
	public String getLiteral() {
		return literal;
	}
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ArrayList<Expression> getMethod_args() {
		return method_args;
	}
	public void setMethod_args(ArrayList<Expression> method_args) {
		this.method_args = method_args;
	}
	public Type getNew_type() {
		return new_type;
	}
	public void setNew_type(Type new_type) {
		this.new_type = new_type;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public ArrayList<Variable> getVariable_decls() {
		return variable_decls;
	}
	public void setVariable_decls(ArrayList<Variable> variable_decls) {
		this.variable_decls = variable_decls;
	}
	
	

}
