package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;

public interface Instruction {
	public List<BinaryValue> toBinaryValues();
}
