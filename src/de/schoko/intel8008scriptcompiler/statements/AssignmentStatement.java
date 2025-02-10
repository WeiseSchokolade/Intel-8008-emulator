package de.schoko.intel8008scriptcompiler.statements;

import java.util.ArrayList;
import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.expression.Expression;
import de.schoko.intel8008scriptcompiler.generation.GenerationContext;

public record AssignmentStatement(VariableReference reference, Expression value) implements Statement {
	@Override
	public List<BinaryValue> toInstructions(GenerationContext context) {
		List<BinaryValue> values = new ArrayList<>();
		RegisterLocation registerLocation = context.evaluateExpression(value, values);
		short variableLocation = context.getVariableLocation(reference);
		values.addAll(List.of(
				BIN.MVI(RegisterLocation.H),
				BIN.VAL(context.getHighByte(variableLocation)),
				BIN.MVI(RegisterLocation.L),
				BIN.VAL(context.getLowByte(variableLocation)),
				BIN.MOV(RegisterLocation.M, registerLocation)
				));
		return values;
	}
	
}
