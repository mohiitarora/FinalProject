package com.boa.ast.framework;

import java.util.ArrayList;

public class Statement {

	private Expression condition;
	private Expression expression;
	private ArrayList<Expression> initializations;
	private StatementKind kind;
	private ArrayList<Statement> statements;
	private Decleration type_declaration;
	private Variable variable_declaration;
	private ArrayList<Expression> updates;
	
	public Expression getCondition() {
		return condition;
	}
	public void setCondition(Expression condition) {
		this.condition = condition;
	}
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	public ArrayList<Expression> getInitializations() {
		return initializations;
	}
	public void setInitializations(ArrayList<Expression> initializations) {
		this.initializations = initializations;
	}
	public StatementKind getKind() {
		return kind;
	}
	public void setKind(StatementKind kind) {
		this.kind = kind;
	}
	public ArrayList<Statement> getStatements() {
		return statements;
	}
	public void setStatements(ArrayList<Statement> statements) {
		this.statements = statements;
	}
	public Decleration getType_declaration() {
		return type_declaration;
	}
	public void setType_declaration(Decleration type_declaration) {
		this.type_declaration = type_declaration;
	}
	public Variable getVariable_declaration() {
		return variable_declaration;
	}
	public void setVariable_declaration(Variable variable_declaration) {
		this.variable_declaration = variable_declaration;
	}
	public ArrayList<Expression> getUpdates() {
		return updates;
	}
	public void setUpdates(ArrayList<Expression> updates) {
		this.updates = updates;
	}
	
	
}
