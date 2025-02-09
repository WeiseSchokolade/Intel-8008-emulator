package de.schoko.intel8008assembler;

public enum TokenType {
	PARAMETER_SEPARATOR(","),
	SUBROUTINE_MARKER(":"),
	REGISTER("[ABCDEHLM]"),
	IDENTIFIER("[a-zA-Z_][a-zA-Z0-9_]*"),
	HEX_LITERAL("0x[0-9a-fA-F]{1,2}h")
	;
	
	private String regex;

	private TokenType(String regex) {
		this.regex = regex;
	}
	
	public String getRegex() {
		return regex;
	}
}
