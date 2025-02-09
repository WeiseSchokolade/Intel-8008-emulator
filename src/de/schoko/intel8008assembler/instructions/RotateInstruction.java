package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;

public record RotateInstruction(boolean left, boolean carry) implements Instruction {

	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(carry ? left ? BIN.RAL() : BIN.RAR() : left ? BIN.RLC() : BIN.RRC());
	}

}
