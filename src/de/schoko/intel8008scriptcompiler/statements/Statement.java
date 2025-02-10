package de.schoko.intel8008scriptcompiler.statements;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008scriptcompiler.generation.GenerationContext;

public interface Statement {
	public List<BinaryValue> toInstructions(GenerationContext context);
}
