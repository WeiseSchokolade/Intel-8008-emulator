package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;

// TODO: Implement all of this
public class CompilerBackend {
	public List<BinaryValue> generateCode(ParseResult parseResult) {
		List<?> mappedMemory = mapMemory(parseResult);
		return generateMemory(mappedMemory);
	}
	
	List<?> mapMemory(ParseResult parseResult) {
		return null;
	}
	
	List<BinaryValue> generateMemory(List<?> mappedMemory) {
		return null;
	}
	
}
