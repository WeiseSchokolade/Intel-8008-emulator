package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008scriptcompiler.Token;

public record VariableReference(Token identifier) {
	@Override
	public final String toString() {
		return identifier.value();
	}
}
