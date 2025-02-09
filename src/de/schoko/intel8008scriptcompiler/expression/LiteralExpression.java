package de.schoko.intel8008scriptcompiler.expression;

import de.schoko.intel8008scriptcompiler.Token;

public record LiteralExpression(Token value) implements Expression {
	@Override
	public final String toString() {
		return value.value();
	}
}
