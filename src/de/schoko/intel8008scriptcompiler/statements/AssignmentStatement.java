package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008scriptcompiler.expression.Expression;

public record AssignmentStatement(VariableReference reference, Expression value) implements Statement {
}
