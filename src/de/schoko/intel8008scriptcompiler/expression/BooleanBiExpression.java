package de.schoko.intel8008scriptcompiler.expression;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.Condition;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Token;
import de.schoko.intel8008scriptcompiler.Variable;

public record BooleanBiExpression(Expression a, Token condition, Expression b) {
	public Condition generate(GenerationContext context) {
		Variable aVariable = context.allocateVariable();
		Variable bVariable = context.allocateVariable();
		a.generate(context, aVariable);
		b.generate(context, bVariable);
		
		String condition = switch (this.condition.value()) {
		case "==" -> "Z";
		default -> throw new IllegalArgumentException("Unknown condition: " + this.condition.value());
		};
		aVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(BIN.MOV(RegisterLocation.A, RegisterLocation.M)));
		bVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(BIN.CMP(RegisterLocation.M)));
		return Condition.valueOf(condition);
	}
	
	@Override
	public final String toString() {
		return "(" + a.toString() + " " + condition.value() + " " + b + ")";
	}
}
