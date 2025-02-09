package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;

public record RestartInstruction(int loc) implements Instruction {
	
	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(BIN.RST(loc));
	}
	
}
