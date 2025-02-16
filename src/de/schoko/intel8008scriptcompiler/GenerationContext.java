package de.schoko.intel8008scriptcompiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.schoko.intel8008assembler.instructions.Instruction;
import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008scriptcompiler.statements.InstructionConstructor;
import de.schoko.intel8008scriptcompiler.statements.Statement;
import de.schoko.intel8008scriptcompiler.statements.VariableReference;

public class GenerationContext {
	private Statement foreshadowedStatement;
	private Statement nextAssociatedStatement;
	
	private List<InstructionConstructor> instructions;
	private Map<InstructionConstructor, Statement> instructionAssociationMap;
	private Map<VariableReference, Variable> variableMap;
	private List<Variable> variables;
	
	public GenerationContext() {
		instructions = new ArrayList<>();
		instructionAssociationMap = new HashMap<>();
		variableMap = new HashMap<>();
		variables = new ArrayList<>();
	}
	
	public Variable allocateVariable(VariableReference reference) {
		Variable variable = new Variable(false);
		variableMap.put(reference, variable);
		variables.add(variable);
		return variable;
	}

	public Variable allocateVariable() {
		Variable variable = new Variable(true);
		variableMap.put(new VariableReference(null), variable);
		variables.add(variable);
		return variable;
	}
	
	public Variable getVariable(VariableReference reference) {
		if (variableMap.containsKey(reference)) {
			return variableMap.get(reference);
		} else {
			throw new IllegalArgumentException("Unknown variable: " + reference);
		}
	}
	
	public void mapMemorySize() {
		int instructionSize = 0;
		for (int i = 0; i < instructions.size(); i++) {
			InstructionConstructor instructionConstructor = instructions.get(i);
			Instruction instruction = instructionConstructor.constructInstruction();
			if (instructionAssociationMap.containsKey(instructionConstructor)) {
				instructionAssociationMap.get(instructionConstructor).setAddress((short) instructionSize);
			}
			instructionSize += instruction.toBinaryValues().size();
		}
		instructionSize += 1; // Halt instruction
		for (int i = 0; i < variables.size(); i++) {
			Variable variable = variables.get(i);
			variable.setAddress((short) (instructionSize + i));
		}
	}
	
	public List<BinaryValue> generateMemory() {
		List<BinaryValue> values = new ArrayList<BinaryValue>();
		for (int i = 0; i < instructions.size(); i++) {
			Instruction instruction = instructions.get(i).constructInstruction();
			values.addAll(instruction.toBinaryValues());
		}
		return values;
	}
	
	public void addInstructionConstructor(InstructionConstructor instructionConstructor) {
		this.instructions.add(instructionConstructor);
		if (nextAssociatedStatement != null) {
			this.instructionAssociationMap.put(instructionConstructor, nextAssociatedStatement);
			nextAssociatedStatement = null;
		}
	}
	
	public void addAssociatedInstructionConstructor(Statement statement, InstructionConstructor instructionConstructor) {
		this.instructions.add(instructionConstructor);
		this.instructionAssociationMap.put(instructionConstructor, statement);
	}
	
	public void associateLastInstructionConstructor(Statement statement) {
		this.instructionAssociationMap.put(instructions.getLast(), statement);
	}
	
	public void associateNextInstructionConstructor(Statement statement) {
		if (nextAssociatedStatement != null) return; // TODO: It might be better to use a list for this
		this.nextAssociatedStatement = statement;
	}
	
	public void setForeshadowedStatement(Statement foreshadowedStatement) {
		this.foreshadowedStatement = foreshadowedStatement;
	}
	
	public Statement getForeshadowedStatement() {
		return foreshadowedStatement;
	}
}
