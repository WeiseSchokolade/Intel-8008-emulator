package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008emulator.RegisterLocation;

public record IncrementInstruction(boolean decrement, RegisterLocation register) implements Instruction {
	
	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(decrement ? BIN.DEC(register) : BIN.INR(register));
	}
	
}
