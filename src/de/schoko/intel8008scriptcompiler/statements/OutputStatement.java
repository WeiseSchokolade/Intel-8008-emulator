package de.schoko.intel8008scriptcompiler.statements;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Variable;
import de.schoko.intel8008scriptcompiler.expression.Expression;

public class OutputStatement extends Statement {
	private Expression value;

	public OutputStatement(Expression value) {
		this.value = value;
	}
	
	@Override
	public void generate(GenerationContext context) {
		Variable variable = context.allocateVariable();
		context.associateNextInstructionConstructor(this);
		value.generate(context, variable);
		variable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(
				BIN.MOV(RegisterLocation.A, RegisterLocation.M),
				BIN.OUT(0)
				));
	}
	
	public Expression getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "OutputStatement[value=" + value + "]";
	}
}
