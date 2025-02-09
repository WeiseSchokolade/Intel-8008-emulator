package de.schoko.intel8008scriptcompiler.expression;

import de.schoko.intel8008scriptcompiler.Token;

public record BiExpression(Expression a, Token operator, Expression b, boolean bracketed) implements Expression {
	@Override
	public final String toString() {
		return "(" + a.toString() + " " + operator.value() + " " + b + ")";
	}
}
