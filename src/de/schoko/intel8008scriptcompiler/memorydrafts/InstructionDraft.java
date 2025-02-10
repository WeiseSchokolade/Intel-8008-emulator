package de.schoko.intel8008scriptcompiler.memorydrafts;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;

public interface InstructionDraft extends MemoryDraft {
	public List<BinaryValue> toBinary();
	
}
