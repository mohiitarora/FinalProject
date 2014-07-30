package com.boa.ast.framework;

import java.util.ArrayList;

public class Decleration {

	private ArrayList<Variable> fields;
	private ArrayList<Type> generic_parameters;
	private ArrayList<Method> methods;
	private ArrayList<Modifier> modifiers;
	private String name;
	private TypeKind kind; 
	private ArrayList<Decleration> nested_declarations;
	private ArrayList<Type> parents;
	
	public String toString(){
		StringBuffer string = new StringBuffer();
		string.append("METHODS : "+methods.toString()+"\n");
		return string.toString();
	}
	
	public ArrayList<Variable> getFields() {
		return fields;
	}
	public void setFields(ArrayList<Variable> fields) {
		this.fields = fields;
	}
	public ArrayList<Type> getGeneric_parameters() {
		return generic_parameters;
	}
	public void setGeneric_parameters(ArrayList<Type> generic_parameters) {
		this.generic_parameters = generic_parameters;
	}
	public ArrayList<Method> getMethods() {
		return methods;
	}
	public void setMethods(ArrayList<Method> methods) {
		this.methods = methods;
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
	public TypeKind getKind() {
		return kind;
	}
	public void setKind(TypeKind kind) {
		this.kind = kind;
	}
	public ArrayList<Decleration> getNested_declarations() {
		return nested_declarations;
	}
	public void setNested_declarations(ArrayList<Decleration> nested_declarations) {
		this.nested_declarations = nested_declarations;
	}
	public ArrayList<Type> getParents() {
		return parents;
	}
	public void setParents(ArrayList<Type> parents) {
		this.parents = parents;
	}
}
