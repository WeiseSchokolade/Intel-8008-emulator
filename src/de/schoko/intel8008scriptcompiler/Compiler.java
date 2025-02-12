package de.schoko.intel8008scriptcompiler;

import java.util.ArrayList;
import java.util.List;

import de.schoko.intel8008assembler.Subroutine;
import de.schoko.intel8008scriptcompiler.expression.BiExpression;
import de.schoko.intel8008scriptcompiler.expression.Expression;
import de.schoko.intel8008scriptcompiler.expression.LiteralExpression;
import de.schoko.intel8008scriptcompiler.expression.VariableExpression;
import de.schoko.intel8008scriptcompiler.statements.AllocateStatement;
import de.schoko.intel8008scriptcompiler.statements.AssignmentStatement;
import de.schoko.intel8008scriptcompiler.statements.Statement;
import de.schoko.intel8008scriptcompiler.statements.VariableReference;

public class Compiler {
	
	List<Subroutine> parseSubroutines(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type() == TokenType.COMMENT) {
				tokens.remove(i);
				i--;
			}
		}
		int tokenIndex = 0;
		while (tokenIndex < tokens.size()) {
			Token token = tokens.get(tokenIndex++);
			assertType(token, TokenType.SUBROUTINE_KEYWORD);
			Token identifier = assertType(tokens.get(tokenIndex++), TokenType.IDENTIFIER);
			assertType(tokens.get(tokenIndex++), TokenType.OPEN_EXPRESSION_BRACKET);
			// TODO: Implement subroutine parser that accepts parameters
		}
		return null;
	}
	
	List<Statement> parseStatements(List<Token> tokens, List<VariableReference> outsideReferences) {
		List<VariableReference> references = new ArrayList<>(outsideReferences);
		List<Statement> statements = new ArrayList<>();
		int tokenIndex = 0;
		while (tokenIndex < tokens.size()) {
			Token token = tokens.get(tokenIndex++);
			if (token.type() == TokenType.VARIABLE_KEYWORD) { // Recognize variable declaration
				Token identifier = assertType(tokens.get(tokenIndex), TokenType.IDENTIFIER);
				AllocateStatement statement = new AllocateStatement(identifier);
				statements.add(statement);
				references.add(statement.reference());
				if (tokens.get(tokenIndex + 1).type() == TokenType.STATEMENT_SEPARATOR) {
					continue;
				}
				token = tokens.get(tokenIndex++);
			}
			if (token.type() == TokenType.IDENTIFIER && tokens.get(tokenIndex).type() == TokenType.ASSIGNMENT_OPERATOR) {
				tokenIndex++; // Skip operator
				VariableReference reference = null;
				for (int i = 0; i < references.size(); i++) {
					reference = references.get(i);
					if (reference.name().equals(token.value())) break;
				}
				if (reference == null) throw new IllegalArgumentException("Unknown variable reference: " + reference);
				List<Token> expressionTokens = new ArrayList<>();
				for (; tokenIndex < tokens.size(); tokenIndex++) {
					token = tokens.get(tokenIndex);
					if (token.type() == TokenType.STATEMENT_SEPARATOR) {
						tokenIndex++;
						break;
					}
					expressionTokens.add(token);
				}
				AssignmentStatement assignmentStatement = new AssignmentStatement(reference, parseExpression(expressionTokens, references));
				statements.add(assignmentStatement);
			}
		}
		return statements;
	}
	
	Expression parseExpression(List<Token> tokens, List<VariableReference> references) {
		if (tokens.size() == 1) {
			Token token = tokens.get(0);
			switch (token.type()) {
			case DEC_LITERAL:
			case HEX_LITERAL:
				return new LiteralExpression(token);
			case IDENTIFIER:
				for (int i = 0; i < references.size(); i++) {
					VariableReference reference = references.get(i);
					if (reference.name().equals(token.value())) {
						return new VariableExpression(reference);
					}
				}
				throw new IllegalArgumentException("Unknown variable: " + token);
			default:
				throw new IllegalArgumentException("Unparsable expression: " + token);
			}
		}
		Expression leftHandExpression;
		int tokenIndex = 0;
		if (tokens.get(tokenIndex).type() == TokenType.OPEN_EXPRESSION_BRACKET) {
			tokenIndex++;
			List<Token> bracketContent = new ArrayList<>();
			int openBrackets = 1;
			for (; tokenIndex < tokens.size(); tokenIndex++) {
				Token token = tokens.get(tokenIndex);
				if (token.type() == TokenType.OPEN_EXPRESSION_BRACKET) {
					openBrackets++;
				} else if (token.type() == TokenType.CLOSE_EXPRESSION_BRACKET) {
					openBrackets--;
				}
				if (openBrackets > 0) {
					bracketContent.add(token);
				} else break;
			}
			leftHandExpression = parseExpression(bracketContent, references);
			tokenIndex++; // Ignore closing bracket
			if (tokenIndex >= tokens.size()) {
				if (leftHandExpression instanceof BiExpression biExpression) {
					return new BiExpression(biExpression.a(), biExpression.operator(), biExpression.b(), true);
				}
				return leftHandExpression;
			}
		} else {
			leftHandExpression = parseExpression(tokens.subList(0, ++tokenIndex), references);
		}
		Token operator = tokens.get(tokenIndex++);
		Expression rightHandExpression = parseExpression(tokens.subList(tokenIndex, tokens.size()), references);
		return reverseNesting(leftHandExpression, operator, rightHandExpression);
	}
	
	BiExpression reverseNesting(Expression leftHandExpression, Token operator, Expression rightHandExpression) {
		if (rightHandExpression instanceof BiExpression biExpression) {
			TokenType rightHandOperatorType = biExpression.operator().type();
			if (rightHandOperatorType == TokenType.LINE_OPERATOR && !biExpression.bracketed()) {
				return new BiExpression(reverseNesting(leftHandExpression, operator, biExpression.a()), biExpression.operator(), biExpression.b(), false);
			}
		}
		return new BiExpression(leftHandExpression, operator, rightHandExpression, false);
		
	}

	List<Token> tokenize(String program) {
		List<Token> tokens = new ArrayList<>();
		
		int startIndex = 0;
		int index = 0;
		TokenType[] tokenTypes = TokenType.values();
		while (index < program.length()) {
			remainingTypeMatcher: {
				String substring = program.substring(startIndex, index + 1).trim();
				index++;
				
				for (int i = 0; i < tokenTypes.length; i++) {
					TokenType type = tokenTypes[i];
					if (substring.matches(type.getRegex())) {
						if (index + 1 < program.length()) {
							String nextSubstring = program.substring(startIndex, index + 1).trim();
							for (int j = 0; j < tokenTypes.length; j++) {
								TokenType nextType = tokenTypes[j];
								if (nextSubstring.matches(nextType.getRegex())) {
									break remainingTypeMatcher;
								}
							}
						}
						// No possible token for string that would be loaded next left
						tokens.add(new Token(substring, type, startIndex));
						startIndex = index;
						continue;
					}
				}
			}
		}
		return tokens;
	}

	public Token assertType(Token token, TokenType type) {
		if (token.type() == type) {
			return token;
		} else {
			throw new IllegalArgumentException("Token " + token + " is not of type " + type);
		}
	}
	
}
