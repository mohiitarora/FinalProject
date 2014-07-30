package com.boa.ast.framework;

import java.util.ArrayList;


public class Method {

	private ArrayList<Variable> variables;
	private ArrayList<Type> exception_types;
	private ArrayList<Type> generic_parameters;
	private ArrayList<Modifier> modifiers;
	private String name;
	private Type return_type;
	private ArrayList<Statement> statements;
	
	public String toString(){
		StringBuffer string = new StringBuffer();
		string.append("Method -> : "+name);
		return string.toString();
	}
	
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	public void setVariables(ArrayList<Variable> variables) {
		this.variables = variables;
	}
	public ArrayList<Type> getException_types() {
		return exception_types;
	}
	public void setException_types(ArrayList<Type> exception_types) {
		this.exception_types = exception_types;
	}
	public ArrayList<Type> getGeneric_parameters() {
		return generic_parameters;
	}
	public void setGeneric_parameters(ArrayList<Type> generic_parameters) {
		this.generic_parameters = generic_parameters;
	}
	public ArrayList<Modifier> getModifiers() {
		return modifiers;
	}
	public void setModifiers(ArrayList<Modifier> modifiers) {
		this.modifiers = modifiers;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getReturn_type() {
		return return_type;
	}
	public void setReturn_type(Type return_type) {
		this.return_type = return_type;
	}
	public ArrayList<Statement> getStatements() {
		return statements;
	}
	public void setStatements(ArrayList<Statement> statements) {
		this.statements = statements;
	}
}
