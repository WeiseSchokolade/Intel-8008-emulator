package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Variable;
import de.schoko.intel8008scriptcompiler.expression.Expression;

public class AssignmentStatement extends Statement {
	private VariableReference reference;
	private Expression value;

	public AssignmentStatement(VariableReference reference, Expression value) {
		this.reference = reference;
		this.value = value;
	}
	
	@Override
	public void generate(GenerationContext context) {
		Variable variable = context.getVariable(reference);
		context.associateNextInstructionConstructor(this);
		value.generate(context, variable);
	}
	
	public VariableReference getReference() {
		return reference;
	}
	
	public Expression getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "AssignmentStatement[reference=" + reference + ", value=" + value + "]";
	}
}
