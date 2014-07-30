package com.boa.ast.framework;

public enum Visibility {

	NAMESPACE	,
	PRIVATE	,
	PROTECTED	,
	PUBLIC	;
	
	public static Visibility getVisibility(String visibility){
		
		if(visibility.equals("public")){
			return Visibility.PUBLIC;
		}else if(visibility.equals("private")){
			return Visibility.PRIVATE;
		}else if(visibility.equals("protected")){
			return Visibility.PROTECTED;
		}
		return Visibility.NAMESPACE;
	}
}
