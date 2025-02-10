package de.schoko.intel8008scriptcompiler.generation;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008emulator.RegisterLocation;
import de.schoko.intel8008scriptcompiler.expression.Expression;
import de.schoko.intel8008scriptcompiler.statements.NativeVariable;
import de.schoko.intel8008scriptcompiler.statements.VariableReference;

// TODO: Implement this
public interface GenerationContext {
	public short getVariableLocation(VariableReference reference);
	public short getVariableLocation(NativeVariable nativeVariable);
	
	public RegisterLocation evaluateExpression(Expression expression, List<BinaryValue> valueDestination);
	
	public byte getHighByte(short location);
	public byte getLowByte(short location);
}
