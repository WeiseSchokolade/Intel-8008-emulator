package de.schoko.intel8008assembler;

import java.util.ArrayList;
import java.util.List;

import de.schoko.intel8008assembler.instructions.Instruction;
import de.schoko.intel8008assembler.instructions.SubroutineInstruction;

public class Subroutine {
	private String name;
	private List<Instruction> instructions;
	private int size;
	private int generationIndex;

	public Subroutine(String name) {
		this.name = name;
		this.instructions = new ArrayList<>();
	}
	
	public void addInstruction(Instruction instruction) {
		this.instructions.add(instruction);
	}
	
	public String getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}
	
	public int calculateSize() {
		size = 0;
		for (int i = 0; i < instructions.size(); i++) {
			Instruction instruction = instructions.get(i);
			size += instruction.toBinaryValues().size();
			if (instruction instanceof SubroutineInstruction) size += 2;
		}
		return size;
	}
	
	public void setGenerationIndex(int generationIndex) {
		this.generationIndex = generationIndex;
	}
	
	public int getGenerationIndex() {
		return generationIndex;
	}
	
	public List<Instruction> getInstructions() {
		return instructions;
	}
}
