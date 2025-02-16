package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008assembler.instructions.Instruction;

public interface InstructionConstructor {
	public Instruction constructInstruction();
}
