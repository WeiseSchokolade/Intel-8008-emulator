package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;

public record ConditionalReturnInstruction(String condition) implements Instruction {

	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(switch (condition.substring(1)) {
		case "NC" -> BIN.RNC();
		case "NZ" -> BIN.RZ();
		case "P" -> BIN.RP();
		case "PO" -> BIN.RPO();
		case "C" -> BIN.RC();
		case "Z" -> BIN.RZ();
		case "M" -> BIN.RM();
		case "PE" -> BIN.RPE();
		default -> throw new IllegalArgumentException("Unexpected value: " + condition.substring(1));
		});
	}
}
