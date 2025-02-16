package de.schoko.intel8008scriptcompiler.expression;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Token;
import de.schoko.intel8008scriptcompiler.Variable;

public record LiteralExpression(Token value) implements Expression {
	@Override
	public final String toString() {
		return value.value();
	}

	@Override
	public void generate(GenerationContext context, Variable destinationVariable) {
		destinationVariable.generateWriteAddressToHL(context);
		context.addInstructionConstructor(() -> () -> List.of(
					BIN.MVI(RegisterLocation.M),
					BIN.VAL((value.value().endsWith("h") ?
							Integer.parseInt(value.value().substring(2, value.value().length() - 1), 16) : 
							Integer.parseInt(value.value())
							))
					)
				);
	}
}
