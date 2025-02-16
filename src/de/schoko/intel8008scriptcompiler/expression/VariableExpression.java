package de.schoko.intel8008scriptcompiler.expression;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Variable;
import de.schoko.intel8008scriptcompiler.statements.VariableReference;

public record VariableExpression(VariableReference reference) implements Expression {
	@Override
	public void generate(GenerationContext context, Variable destinationVariable) {
		Variable variable = context.getVariable(reference);
		variable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(BIN.MOV(RegisterLocation.A, RegisterLocation.M)));
		destinationVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(BIN.MOV(RegisterLocation.M, RegisterLocation.A)));
	}
}
