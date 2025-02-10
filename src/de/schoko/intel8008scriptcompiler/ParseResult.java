package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008scriptcompiler.statements.NativeVariable;
import de.schoko.intel8008scriptcompiler.statements.VariableReference;

public record ParseResult(List<VariableReference> globalVariables, List<Subroutine> subroutines, List<NativeVariable> nativeVariables) {
	// TODO: Move native variables into generation context
	public NativeVariable requestNativeVariable(String name, short defaultValue) {
		for (int i = 0; i < nativeVariables.size(); i++) {
			NativeVariable variable = nativeVariables.get(i);
			if (variable.identifier().equals(name)) {
				return variable;
			}
		}
		NativeVariable variable = new NativeVariable(name, defaultValue);
		nativeVariables.add(variable);
		return variable;
	}
}
