package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008scriptcompiler.statements.Statement;
import de.schoko.intel8008scriptcompiler.statements.VariableReference;

public record Subroutine(Token identifier, List<VariableReference> parameters, List<Statement> statements) {

}
