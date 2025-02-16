package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.RegisterLocation;

public class Variable {
	private short address;
	private boolean temporary;
	
	public Variable(boolean temporary) {
		this.temporary = temporary;
	}
	
	public void generateWriteAddressToHL(GenerationContext context) {
		context.addInstructionConstructor(() -> {
			return () -> List.of(
				BIN.MVI(RegisterLocation.L),
				BIN.VAL(address & 0xFF),
				BIN.MVI(RegisterLocation.H),
				BIN.VAL(address & 0xFF00)
			);
		});
	}
	
	public void setAddress(short address) {
		this.address = address;
	}
	
	public short getAddress() {
		return address;
	}
	
	public boolean isTemporary() {
		return temporary;
	}
}
