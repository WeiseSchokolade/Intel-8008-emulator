package de.schoko.intel8008scriptcompiler.expression;

import de.schoko.intel8008scriptcompiler.statements.VariableReference;

public record VariableExpression(VariableReference reference) implements Expression {

}
