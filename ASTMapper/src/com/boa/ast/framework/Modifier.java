package com.boa.ast.framework;

import java.util.ArrayList;


public class Modifier {

	private ArrayList<String> annotation_members;
	private String annotation_name;
	private ArrayList<Expression> annotation_values;
	private ModifierKind kind;
	private String other;
	private Visibility visibility;
	
	public ArrayList<String> getAnnotation_members() {
		return annotation_members;
	}
	public void setAnnotation_members(ArrayList<String> annotation_members) {
		this.annotation_members = annotation_members;
	}
	public String getAnnotation_name() {
		return annotation_name;
	}
	public void setAnnotation_name(String annotation_name) {
		this.annotation_name = annotation_name;
	}
	public ArrayList<Expression> getAnnotation_values() {
		return annotation_values;
	}
	public void setAnnotation_values(ArrayList<Expression> annotation_values) {
		this.annotation_values = annotation_values;
	}
	public ModifierKind getKind() {
		return kind;
	}
	public void setKind(ModifierKind kind) {
		this.kind = kind;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public Visibility getVisibility() {
		return visibility;
	}
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	
	
}
