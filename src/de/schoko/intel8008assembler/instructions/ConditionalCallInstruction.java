package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;

public record ConditionalCallInstruction(boolean jump, String subroutine, String condition) implements SubroutineInstruction {
	
	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(switch (condition.substring(1)) {
		case "NC" -> (jump) ? BIN.JNC() : BIN.CNC();
		case "NZ" -> (jump) ? BIN.JNZ() : BIN.CZ();
		case "P" -> (jump) ? BIN.JP() : BIN.CP();
		case "PO" -> (jump) ? BIN.JPO() : BIN.CPO();
		case "C" -> (jump) ? BIN.JC() : BIN.CC();
		case "Z" -> (jump) ? BIN.JZ() : BIN.CZ();
		case "M" -> (jump) ? BIN.JM() : BIN.CM();
		case "PE" -> (jump) ? BIN.JPE() : BIN.CPE();
		default -> throw new IllegalArgumentException("Unexpected value: " + condition.substring(1));
		});
	}
	
}
