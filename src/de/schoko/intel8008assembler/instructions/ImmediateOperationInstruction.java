package de.schoko.intel8008assembler.instructions;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;

public record ImmediateOperationInstruction(String operation, short value) implements Instruction {

	@Override
	public List<BinaryValue> toBinaryValues() {
		return List.of(
				switch (operation) {
				case "ADI" -> BIN.ADI();
				case "ACI" -> BIN.ACI();
				case "SUI" -> BIN.SUI();
				case "SBI" -> BIN.SBI();
				case "ANI" -> BIN.ANI();
				case "XRI" -> BIN.XRI();
				case "ORI" -> BIN.ORI();
				case "CPI" -> BIN.CPI();
				default -> throw new IllegalArgumentException("Unexpected value: " + operation);
				},
				BIN.VAL(value)
				);
	}

}
