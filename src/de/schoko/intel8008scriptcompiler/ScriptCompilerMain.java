package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008scriptcompiler.statements.Statement;

public class ScriptCompilerMain {
	public static void main(String[] args) {
		Compiler compiler = new Compiler();
		List<Token> tokens = compiler.tokenize("""
		var c = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10;
				""");
		List<Statement> statements = compiler.parseStatements(tokens, List.of());
		System.out.println(statements);
	}
}
