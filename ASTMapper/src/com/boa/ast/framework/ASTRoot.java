package com.boa.ast.framework;

import java.util.ArrayList;

public class ASTRoot {

	private ArrayList<String> imports;
	private ArrayList<Namespace> namespaces;
	
	public ArrayList<String> getImports() {
		return imports;
	}
	public void setImports(ArrayList<String> imports) {
		this.imports = imports;
	}
	public ArrayList<Namespace> getNamespaces() {
		return namespaces;
	}
	public void setNamespaces(ArrayList<Namespace> namespaces) {
		this.namespaces = namespaces;
	}
	
	public String toString(){
		StringBuffer astValue = new StringBuffer();
		astValue.append("##############################\n");
		astValue.append("IMPORTS:"+imports.toString()+"\n");
		astValue.append("----------------------------------\n");
		astValue.append("NAMESPACE Details:"+namespaces.toString()+"\n");
		astValue.append("##############################\n");
		
		return astValue.toString();
	}
}
