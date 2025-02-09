package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008emulator.RegisterLocation;

public record OperationInstruction(String operation, RegisterLocation source) implements Instruction {

	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(
				switch (operation) {
				case "ADD" -> BIN.ADD(source);
				case "ADC" -> BIN.ADC(source);
				case "SUB" -> BIN.SUB(source);
				case "SBB" -> BIN.SBB(source);
				case "ANA" -> BIN.ANA(source);
				case "XRA" -> BIN.XRA(source);
				case "ORA" -> BIN.OR(source);
				case "CMP" -> BIN.CMP(source);
				default -> throw new IllegalArgumentException("Unexpected value: " + operation);
				}
				);
	}
}
