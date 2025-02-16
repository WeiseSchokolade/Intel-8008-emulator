package de.schoko.intel8008scriptcompiler.expression;

import java.util.List;

import de.schoko.intel8008assembler.instructions.OperationInstruction;
import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Token;
import de.schoko.intel8008scriptcompiler.Variable;

public record BiExpression(Expression a, Token operator, Expression b, boolean bracketed) implements Expression {
	@Override
	public final String toString() {
		return "(" + a.toString() + " " + operator.value() + " " + b + ")";
	}
	
	@Override
	public void generate(GenerationContext context, Variable destinationVariable) {
		Variable aVariable = context.allocateVariable();
		Variable bVariable = context.allocateVariable();
		a.generate(context, aVariable);
		b.generate(context, bVariable);
		
		String operator = switch (this.operator.value()) {
		case "+" -> "ADD";
		case "-" -> "SUB";
		// TODO: Add division/multiplication
		default -> throw new IllegalArgumentException("Unknown operator: " + this.operator.value());
		};
		aVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(BIN.MOV(RegisterLocation.A, RegisterLocation.M)));
		bVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> new OperationInstruction(operator, RegisterLocation.M));
		destinationVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(BIN.MOV(RegisterLocation.M, RegisterLocation.A)));
	}
}
