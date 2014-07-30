package com.boa.ast.framework;

import java.util.ArrayList;


public class Variable {

	private String name;
	private Type variable_type;
	private ArrayList<Modifier> modifiers;
	private Expression initializer;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getVariable_type() {
		return variable_type;
	}
	public void setVariable_type(Type variable_type) {
		this.variable_type = variable_type;
	}
	public ArrayList<Modifier> getModifiers() {
		return modifiers;
	}
	public void setModifiers(ArrayList<Modifier> modifiers) {
		this.modifiers = modifiers;
	}
	public Expression getInitializer() {
		return initializer;
	}
	public void setInitializer(Expression initializer) {
		this.initializer = initializer;
	}
}
