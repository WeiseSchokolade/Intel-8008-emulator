package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Token;

public class AllocateStatement extends Statement {
	private VariableReference reference;
	private Token identifier;

	public AllocateStatement(VariableReference reference, Token identifier) {
		this.reference = reference;
		this.identifier = identifier;
	}
	
	public AllocateStatement(Token identifier) {
		this(new VariableReference(identifier.value()), identifier);
	}

	@Override
	public void generate(GenerationContext context) {
		context.associateNextInstructionConstructor(this);
		context.allocateVariable(reference);
	}
	
	public VariableReference getReference() {
		return reference;
	}
	
	public Token getIdentifier() {
		return identifier;
	}

	@Override
	public String toString() {
		return "AllocateStatement[reference=" + reference + ", identifier=" + identifier + "]";
	}
}
