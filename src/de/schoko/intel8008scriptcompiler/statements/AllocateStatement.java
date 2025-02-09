package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008scriptcompiler.Token;

public record AllocateStatement(VariableReference reference, Token identifier) implements Statement {
	public AllocateStatement(Token identifier) {
		this(new VariableReference(identifier.value()), identifier);
	}
}
