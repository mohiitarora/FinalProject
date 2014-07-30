package com.boa.ast.framework;

import java.util.ArrayList;

public class Namespace {

	private ArrayList<Modifier> modifiers;
	private ArrayList<Decleration> declerations;
	private String name;
	
	public ArrayList<Modifier> getModifiers() {
		return modifiers;
	}
	public void setModifiers(ArrayList<Modifier> modifiers) {
		this.modifiers = modifiers;
	}
	public ArrayList<Decleration> getDeclerations() {
		return declerations;
	}
	public void setDeclerations(ArrayList<Decleration> declerations) {
		this.declerations = declerations;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		StringBuffer string = new StringBuffer();
		string.append("Package Name: "+name+" {} ");
		string.append("Declerations: "+declerations.toString());
		return string.toString();
	}
	
}
