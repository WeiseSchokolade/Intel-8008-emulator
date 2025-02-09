package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;

public record CallInstruction(boolean jump, String subroutine) implements SubroutineInstruction {
	
	@Override
	public List<BinaryValue> toBinaryValues() {
		if (jump) {
			return List.of(BIN.JMP());
		} else {
			return List.of(BIN.CALL());
		}
	}
	
}
