package de.schoko.intel8008scriptcompiler.expression;

import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.Variable;

public interface Expression {
	public void generate(GenerationContext context, Variable destinationVariable);
}
