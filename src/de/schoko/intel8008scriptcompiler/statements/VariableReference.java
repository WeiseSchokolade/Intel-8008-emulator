package de.schoko.intel8008scriptcompiler.statements;

public record VariableReference(String name) {
	@Override
	public final String toString() {
		return name;
	}
}
