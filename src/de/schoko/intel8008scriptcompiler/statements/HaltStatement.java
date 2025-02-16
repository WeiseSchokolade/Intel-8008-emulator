package de.schoko.intel8008scriptcompiler.statements;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008scriptcompiler.GenerationContext;

public class HaltStatement extends Statement {

	@Override
	public void generate(GenerationContext context) {
		context.addAssociatedInstructionConstructor(this, () -> () -> List.of(BIN.HLT()));
	}

}
