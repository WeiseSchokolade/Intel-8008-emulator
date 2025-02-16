package de.schoko.intel8008scriptcompiler;

public enum TokenType {
	SUBROUTINE_KEYWORD("subroutine"),
	IF_KEYWORD("if"),
	WHILE_KEYWORD("while"),
	OUTPUT_KEYWORD("output"),
	VARIABLE_KEYWORD("var"),
	PARAMETER_SEPARATOR(","),
	STATEMENT_SEPARATOR(";"),
	IDENTIFIER("[a-zA-Z_][a-zA-Z0-9_]*"),
	DEC_LITERAL("[0-9]{1,3}"),
	HEX_LITERAL("0x[0-9a-fA-F]{1,2}h"),
	EQUALS_OPERATOR("=="),
	ASSIGNMENT_OPERATOR("="),
	DOT_OPERATOR("[*/]"),
	LINE_OPERATOR("[+\\-]"),
	OPEN_STATEMENT_BRACKET("\\{"),
	CLOSE_STATEMENT_BRACKET("}"),
	OPEN_EXPRESSION_BRACKET("\\("),
	CLOSE_EXPRESSION_BRACKET("\\)"),
	COMMENT("/\\*.*\\*/")
	;
	
	private String regex;

	private TokenType(String regex) {
		this.regex = regex;
	}
	
	public String getRegex() {
		return regex;
	}
}
