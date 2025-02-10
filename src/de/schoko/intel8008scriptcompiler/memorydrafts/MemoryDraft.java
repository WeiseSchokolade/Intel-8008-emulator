package de.schoko.intel8008scriptcompiler.memorydrafts;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;

public interface MemoryDraft {
	public int size();
	public List<BinaryValue> toBinaryValues();
}
