package de.schoko.intel8008assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.schoko.intel8008assembler.instructions.CallInstruction;
import de.schoko.intel8008assembler.instructions.ConditionalCallInstruction;
import de.schoko.intel8008assembler.instructions.ConditionalReturnInstruction;
import de.schoko.intel8008assembler.instructions.ImmediateOperationInstruction;
import de.schoko.intel8008assembler.instructions.IncrementInstruction;
import de.schoko.intel8008assembler.instructions.Instruction;
import de.schoko.intel8008assembler.instructions.OperationInstruction;
import de.schoko.intel8008assembler.instructions.RestartInstruction;
import de.schoko.intel8008assembler.instructions.RotateInstruction;
import de.schoko.intel8008assembler.instructions.SubroutineInstruction;
import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008emulator.RegisterLocation;

public class Assembler {
	public List<BinaryValue> assemble(String program) {
		List<Subroutine> subroutines = frontEnd(program);
		return backEnd(subroutines);
	}
	
	List<Subroutine> frontEnd(String program) {
		List<Token> tokens = tokenize(program);
		List<Subroutine> subroutines = parse(tokens);
		return subroutines;
	}
	
	List<Token> tokenize(String program) {
		List<Token> tokens = new ArrayList<>();
		
		int startIndex = 0;
		int index = 0;
		TokenType[] tokenTypes = TokenType.values();
		while (index < program.length()) {
			subroutine: {
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
									break subroutine;
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
	
	List<Subroutine> parse(List<Token> tokens) {
		List<Subroutine> subroutines = new ArrayList<>();
		Subroutine currentSubroutine = new Subroutine(null);
		subroutines.add(currentSubroutine);
		int index = 0;
		while (index < tokens.size()) {
			Token token = tokens.get(index);
			try {
				switch (token.type()) {
				case IDENTIFIER:
					if (index + 1 < tokens.size() && tokens.get(index + 1).type() == TokenType.SUBROUTINE_MARKER) {
						currentSubroutine = new Subroutine(token.value());
						subroutines.add(currentSubroutine);
						index++;
						break;
					}
					Instruction instruction = switch (token.value()) {
					case "HLT" -> () -> List.of(BIN.HLT());
					case "JMP" -> {
						yield new CallInstruction(true,
								assertType(tokens.get(++index), TokenType.IDENTIFIER).value()
								);
					}
					case "JNC", "JNZ", "JP", "JPO", "C", "Z", "M", "PE" -> {
						yield new ConditionalCallInstruction(true,
								assertType(tokens.get(++index), TokenType.IDENTIFIER).value(),
								token.value());
					}
					case "CALL" -> {
						yield new CallInstruction(false,
								assertType(tokens.get(++index), TokenType.IDENTIFIER).value()
								);
					}
					case "RST" -> {
						String nextToken = assertType(tokens.get(++index), TokenType.HEX_LITERAL).value();
						yield new RestartInstruction(
								Integer.valueOf(nextToken.substring(2, nextToken.length() - 1), 16)
								);
					}
					case "CNC", "CNZ", "CP", "CPO", "CC", "CZ", "CM", "CPE" -> {
						yield new ConditionalCallInstruction(true,
								assertType(tokens.get(++index), TokenType.IDENTIFIER).value(),
								token.value());
					}
					case "RET" -> () -> List.of(BIN.RET());
					case "RNC", "RNZ", "RP", "RPO", "RC", "RZ", "RM", "RPE" -> {
						yield new ConditionalReturnInstruction(token.value());
					}
					case "MOV" -> {
						Token destinationRegister = assertType(tokens.get(++index), TokenType.REGISTER);
						RegisterLocation destinationRegisterLocation = RegisterLocation.valueOf(destinationRegister.value());
						assertType(tokens.get(++index), TokenType.PARAMETER_SEPARATOR);
						Token sourceRegister = assertType(tokens.get(++index), TokenType.REGISTER);
						if (destinationRegister.value().equals(sourceRegister.value())) throw new IllegalArgumentException("Cannot move data from and to the same register: " + token);
						RegisterLocation sourceRegisterLocation = RegisterLocation.valueOf(sourceRegister.value());
						yield () -> List.of(BIN.MOV(destinationRegisterLocation, sourceRegisterLocation));
					}
					case "MVI" -> {
						Token destinationRegister = assertType(tokens.get(++index), TokenType.REGISTER);
						RegisterLocation destinationRegisterLocation = RegisterLocation.valueOf(destinationRegister.value());
						assertType(tokens.get(++index), TokenType.PARAMETER_SEPARATOR);
						Token valueToken = assertType(tokens.get(++index), TokenType.HEX_LITERAL);
						Integer value = Integer.valueOf(valueToken.value().substring(2, valueToken.value().length() - 1), 16);
						yield () -> List.of(BIN.MVI(destinationRegisterLocation), BIN.VAL(value));
					}
					case "INR" -> {
						Token destinationRegister = assertType(tokens.get(++index), TokenType.REGISTER);
						RegisterLocation destinationRegisterLocation = RegisterLocation.valueOf(destinationRegister.value());
						if (destinationRegister.value().equals("M")) throw new IllegalArgumentException("Cannot use M as register in incrementation: " + destinationRegister);
						yield new IncrementInstruction(false, destinationRegisterLocation);
					}
					case "DEC" -> {
						Token destinationRegister = assertType(tokens.get(++index), TokenType.REGISTER);
						RegisterLocation destinationRegisterLocation = RegisterLocation.valueOf(destinationRegister.value());
						if (destinationRegister.value().equals("M")) throw new IllegalArgumentException("Cannot use M as register in incrementation: " + destinationRegister);
						yield new IncrementInstruction(true, destinationRegisterLocation);
					}
					case "ADD", "ADC", "SUB", "SBB", "ANA", "XRA", "ORA", "CMP" -> {
						Token sourceRegister = assertType(tokens.get(++index), TokenType.REGISTER);
						RegisterLocation sourceRegisterLocation = RegisterLocation.valueOf(sourceRegister.value());
						yield new OperationInstruction(token.value(), sourceRegisterLocation);
					}
					case "ADI", "ACI", "SUI", "SBI", "ANI", "XRI", "ORI", "CPI" -> {
						String nextToken = assertType(tokens.get(++index), TokenType.HEX_LITERAL).value();
						yield new ImmediateOperationInstruction(token.value(), Short.valueOf(nextToken.substring(2, nextToken.length() - 1), 16));
					}
					case "RLC" -> new RotateInstruction(true, false);
					case "RRC" -> new RotateInstruction(false, false);
					case "RAL" -> new RotateInstruction(true, true);
					case "RAR" -> new RotateInstruction(false, true);
					default -> throw new IllegalArgumentException("Unknown instruction type: " + token.value());
					};
					currentSubroutine.addInstruction(instruction);
					break;
				default:
					throw new IllegalArgumentException("Illegal token type: " + token);
				}
			} catch (RuntimeException e) {
				System.err.println(token);
				throw e;
			}
			index++;
		}
		return subroutines;
	}
	
	public Token assertType(Token token, TokenType type) {
		if (token.type() == type) {
			return token;
		} else {
			throw new IllegalArgumentException("Token " + token + " is not of type " + type);
		}
	}
	
	List<BinaryValue> backEnd(List<Subroutine> subroutines) {
		int generationIndex = 0;
		Map<String, Subroutine> subroutineMap = new HashMap<>();
		for (int i = 0; i < subroutines.size(); i++) {
			Subroutine subroutine = subroutines.get(i);
			subroutine.setGenerationIndex(generationIndex);
			generationIndex += subroutine.calculateSize();
			subroutineMap.put(subroutine.getName(), subroutine);
		}
		List<BinaryValue> values = new ArrayList<>();
		for (int i = 0; i < subroutines.size(); i++) {
			Subroutine subroutine = subroutines.get(i);
			List<Instruction> instructions = subroutine.getInstructions();
			for (int j = 0; j < instructions.size(); j++) {
				Instruction instruction = instructions.get(j);
				values.addAll(instruction.toBinaryValues());
				if (instruction instanceof SubroutineInstruction subroutineInstruction) {
					Subroutine calledSubroutine = subroutineMap.get(subroutineInstruction.subroutine());
					int calledGenerationIndex = calledSubroutine.getGenerationIndex();
					int lowByte = calledGenerationIndex % 256;
					int highByte = generationIndex / 256;
					values.add(BIN.VAL(lowByte));
					values.add(BIN.VAL(highByte));
				}
			}
		}
		return values;
	}
}
