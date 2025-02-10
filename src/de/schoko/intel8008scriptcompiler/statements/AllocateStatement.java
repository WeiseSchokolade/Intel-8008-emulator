package de.schoko.intel8008scriptcompiler.statements;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008scriptcompiler.Token;
import de.schoko.intel8008scriptcompiler.generation.GenerationContext;

public record AllocateStatement(VariableReference reference, Token identifier) implements Statement {
	public AllocateStatement(Token identifier) {
		this(new VariableReference(identifier), identifier);
	}

	@Override
	public List<BinaryValue> toInstructions(GenerationContext context) {
		// TODO Auto-generated method stub
		return null;
	}
}
