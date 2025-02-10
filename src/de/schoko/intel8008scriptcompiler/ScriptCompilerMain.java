package de.schoko.intel8008scriptcompiler;

import java.util.List;

public class ScriptCompilerMain {
	public static void main(String[] args) {
		CompilerFrontend compiler = new CompilerFrontend();
		List<Token> tokens = compiler.tokenize("""
				global a;
				
				subroutine main() {
					a = 3 + 5;
				}
				""");
		ParseResult parseResult = compiler.parseSubroutines(tokens);
		System.out.println(parseResult);
	}
}
