package de.schoko.intel8008emulator;

public class BinaryExecutor {
	private byte[] registers = {
			0, // A
			0, // B
			0, // C
			0, // D
			0, // E
			0, // H \ High
			0  // L / Low
	};
	private byte[] memory = new byte[65536];
	private short[] programCounterStack = new short[8];
	private int currentCounter = 0;
	
	private boolean halted;
	private boolean carry;
	private boolean zero;
	private boolean parityEven;
	private boolean negative;
	
	private byte[] inputs = new byte[8];
	private byte[] outputs = new byte[24];
	
	public void step() {
		if (halted) return;
		byte opcode = memory[programCounterStack[currentCounter]];
		compressedStepDump(opcode);
		programCounterStack[currentCounter]++;
		
		if (opcode == -1 || opcode == 0 || opcode == 1) {
			// HALT
			halted = true;
		} else if ((opcode & -15) == 65) {
			// Input
			int port = isolateNumber(opcode, 1, 3);
			registers[0] = inputs[port];
		} else if ((opcode & -63) == 65) {
			// Output
			int port = isolateNumber(opcode, 1, 5) - 8;
			outputs[port] = registers[0];
		} else if ((opcode & -57) == 68) {/* 01xxx100*/ 
			// Jump
			byte lowByte = memory[programCounterStack[currentCounter]];
			byte highByte = memory[programCounterStack[currentCounter] + 1];
			programCounterStack[currentCounter] = (short) (lowByte + highByte * 256);
		} else if ((opcode & -57) == 64) {/* 01xxx000*/
			// Jump if condition
			boolean condition = switch (isolateNumber(opcode, 3, 3)) {
				case 0 -> !carry; // carry = 0
				case 1 -> !zero; // result != 0
				case 2 -> !negative; // sign = 0
				case 3 -> !parityEven; // parity = odd
				case 4 -> carry; // carry = 1
				case 5 -> zero; // result = 0
				case 6 -> negative; // sign = 1
				case 7 -> parityEven; // parity = even
			default -> throw new IllegalArgumentException("Unknown jump condition: " + isolateNumber(opcode, 3, 3));
			};
			if (condition) {
				byte lowByte = memory[programCounterStack[currentCounter]];
				byte highByte = memory[programCounterStack[currentCounter] + 1];
				programCounterStack[currentCounter] = (short) (lowByte + highByte * 256);
			} else {
				programCounterStack[currentCounter] += 2;
			}
		} else if ((opcode & -57) == 70) { /* 01xxx110 */
			// Call
			byte lowByte = memory[programCounterStack[currentCounter]];
			byte highByte = memory[programCounterStack[currentCounter] + 1];
			programCounterStack[currentCounter] += 2;
			currentCounter = (currentCounter + 1) % programCounterStack.length;
			programCounterStack[currentCounter] = (short) (lowByte + highByte * 256);
		} else if ((opcode & -57) == 66) {/* 01xxx010*/
			// Call if condition
			boolean condition = switch (isolateNumber(opcode, 3, 3)) {
				case 0 -> !carry; // carry = 0
				case 1 -> !zero; // result != 0
				case 2 -> !negative; // sign = 0
				case 3 -> !parityEven; // parity = odd
				case 4 -> carry; // carry = 1
				case 5 -> zero; // result = 0
				case 6 -> negative; // sign = 1
				case 7 -> parityEven; // parity = even
			default -> throw new IllegalArgumentException("Unknown call condition: " + isolateNumber(opcode, 3, 3));
			};
			if (condition) {
				byte lowByte = memory[programCounterStack[currentCounter]];
				byte highByte = memory[programCounterStack[currentCounter] + 1];
				programCounterStack[currentCounter] += 2;
				currentCounter = (currentCounter + 1) % programCounterStack.length;
				programCounterStack[currentCounter] = (short) (lowByte + highByte * 256);
			} else {
				programCounterStack[currentCounter] += 2;
			}
		} else if ((opcode & -57) == 7) {
			// Return
			currentCounter = (currentCounter + programCounterStack.length - 1) % programCounterStack.length;
		} else if ((opcode & -57) == 3) {/* 00xxx011*/
			// Return if condition
			boolean condition = switch (isolateNumber(opcode, 3, 3)) {
				case 0 -> !carry; // carry = 0
				case 1 -> !zero; // result != 0
				case 2 -> !negative; // sign = 0
				case 3 -> !parityEven; // parity = odd
				case 4 -> carry; // carry = 1
				case 5 -> zero; // result = 0
				case 6 -> negative; // sign = 1
				case 7 -> parityEven; // parity = even
			default -> throw new IllegalArgumentException("Unknown return condition: " + isolateNumber(opcode, 3, 3));
			};
			if (condition) {
				currentCounter = (currentCounter + programCounterStack.length - 1) % programCounterStack.length;
			}
		} else if ((opcode & -57) == 5) {/* 00AAA101*/
			// Call subroutine at AAA000
			int addr = isolateNumber(opcode, 3, 3) << 16;
			currentCounter = (currentCounter + 1) % programCounterStack.length;
			programCounterStack[currentCounter] = (short) (addr);
		} else if ((opcode & -64) == -64) {/* 11xxxxxx */
			// Load from/into register/memory address
			int toAddress = isolateNumber(opcode, 3, 3);
			int fromAddress = isolateNumber(opcode, 0, 3);
			set(toAddress, get(fromAddress));
		} else if ((opcode & -57) == 6) {/* 00xxx110*/
			// Load register/memory address with the following data
			int toAddress = isolateNumber(opcode, 3, 3);
			set(toAddress, memory[programCounterStack[currentCounter]]);
			programCounterStack[currentCounter]++;
		} else if ((opcode & -64) == -128) {/* 10xxxxxx */
			int operation = isolateNumber(opcode, 3, 3);
			byte a = registers[0];
			byte b = get(isolateNumber(opcode, 0, 3));
			
			stepALU(operation, a, b);
		} else if ((opcode & -57) == 4) {
			// Immediate ALU
			int operation = isolateNumber(opcode, 3, 3);
			byte a = registers[0];
			byte b = memory[programCounterStack[currentCounter]];
			programCounterStack[currentCounter]++;
			
			stepALU(operation, a, b);
		} else if ((opcode & -57) == 0 && (opcode & 56) != 56) {
			// Increment register
			int register = isolateNumber(opcode, 3, 3);
			registers[register]++;
			if (registers[register] > 127) {
				registers[register] -= 256;
			}
		} else if ((opcode & -57) == 1 && (opcode & 56) != 56) {
			// Decrement register
			int register = isolateNumber(opcode, 3, 3);
			registers[register]--;
			if (registers[register] < -128) {
				registers[register] += 256;
			}
		} else if (opcode == 2) {/* 00000010 */
			// Rotate A left
			carry = (registers[0] & (byte) -128) != 0;
			registers[0] <<= 1;
		} else if (opcode == 10) {
			// Rotate A right
			carry = (registers[0] & 1) != 0;
			registers[0] >>= 1;
		} else if (opcode == 18) {
			byte result = (byte) (carry ? 1 : 0);
			result += (registers[0] << 1);
			carry = (registers[0] & (byte) -128) != 0;
			registers[0] = result;
		} else if (opcode == 26) {
			byte result = (byte) (carry ? -128 : 0);
			result += (registers[0] >> 1);
			carry = (registers[0] & 1) != 0;
			registers[0] = result;
		} else {
			System.err.println("Unknown instruction: " + opcode);
		}
	}
	
	public void stepALU(int operation, byte a, byte b) {
		int result = 0;

		switch (operation) {
		case 0: // Add
			result = a + b;
			carry = (result & 0x100) > 0;
			result &= 0xFF;
			break;
		case 1: // Add with carry
			result = a + b + (carry ? 1 : 0);
			carry = (result & 0x100) > 0;
			result &= 0xFF;
			break;
		case 2: // Sub
			//carry = !((a > b) ^ (b < 0) ^ (a < 0));
			result = a + (b ^ 0xFF) + 1;
			carry = (result & 0x100) == 0;
			result &= 0xFF;
			break;
		case 3: // Sub with carry
			result = a + (b ^ 0xFF) + (carry ? 0 : 1);
			carry = (result & 0x100) == 0;
			result &= 0xFF;
			break;
		case 4: // Bitwise And
			result = a & b;
			break;
		case 5: // Bitwise XOr
			result = a ^ b;
			break;
		case 6: // Bitwise OR
			result = a | b;
			break;
		case 7: // Compare
			result = a + ((b & 0xFF) ^ 0xFF) + 1;
			carry = (result & 0x100) == 0;
			result &= 0xFF;
			zero = (result == 0);
			negative = (result & 0x80) == 0x80;
			
			int parityValue = result ^ (result >> 1);
			parityValue = parityValue ^ (parityValue >> 2);
			parityValue = parityValue ^ (parityValue >> 4);
			parityValue = parityValue ^ (parityValue >> 8);
			parityEven = (parityValue & 1) == 1;
			return;
		default:
			System.out.println("Unknown math operation: " + operation);
		}
		zero = (result == 0);
		negative = (result & 0x80) == 0x80;
		
		int parityValue = result ^ (result >> 1);
		parityValue = parityValue ^ (parityValue >> 2);
		parityValue = parityValue ^ (parityValue >> 4);
		parityValue = parityValue ^ (parityValue >> 8);
		parityEven = (parityValue & 1) == 1;
		
		registers[0] = (byte) result;
	}
	
	public byte get(int registerAddress) {
		if (registerAddress == 7) {
			return memory[registers[5] * 256 + registers[6]];
		} else {
			return registers[registerAddress];
		}
	}
	
	public void set(int registerAddress, byte value) {
		if (registerAddress == 7) {
			memory[registers[5] * 256 + registers[6]] = value;
		} else {
			registers[registerAddress] = value;
		}
	}

	public void fillMemory(BinaryValue... values) {
		for (int i = 0; i < values.length; i++) {
			memory[i] = values[i].value();
		}
	}
	
	public void setInput(int port, byte value) {
		inputs[port] = value;
	}

	public byte getOutput(int port) {
		return outputs[port];
	}
	
	public void dump(int amount) {
		System.out.println("=== MEMORY DUMP ===");
		System.out.println("LOC - VALUE");
		int indexLength = (Integer.toHexString(amount)).length();
		for (int i = 0; i < amount; i++) {
			System.out.println(" " + padHexInt(i, indexLength) + " - " + padHexInt(memory[i], 8));
		}
	}

	public void dump(int startIndex, int amount) {
		System.out.println("=== MEMORY DUMP ===");
		System.out.println("LOC - VALUE");
		int indexLength = (Integer.toHexString(amount)).length();
		for (int i = startIndex; i < startIndex + amount; i++) {
			System.out.println(" " + padHexInt(i, indexLength) + " - " + padHexInt(memory[i], 8));
		}
	}

	public void dumpRegisters() {
		System.out.println("=== REGISTER DUMP ===");
		System.out.println("REGISTER - VALUE");
		for (int i = 0; i < registers.length; i++) {
			System.out.println(" " + RegisterLocation.values()[i].name() + " - " + padBinaryInt(registers[i], 8));
		}
	}

	public void dumpStack() {
		System.out.println("=== PC STACK DUMP ===");
		System.out.println("PC ID - PC");
		for (int i = 0; i < programCounterStack.length; i++) {
			System.out.println(" " + i + " - " + padHexInt(programCounterStack[i], 6));
		}
	}

	private void compressedStepDump(byte opcode) {
		System.out.println("PC " + padHexInt(programCounterStack[currentCounter], 2) + " - Depth " + currentCounter + " Opcode " + padBinaryInt(Byte.toUnsignedInt(opcode), 8));
	}
	
	@SuppressWarnings("unused")
	private void dumpOpcode(byte opcode) {
		System.out.println("=== OPCODE DUMP ===");
		System.out.println("Stack depth: " + currentCounter);
		System.out.println("PC: " + Integer.toHexString(programCounterStack[currentCounter]));
		System.out.println("Opcode: " + padBinaryInt(Byte.toUnsignedInt(opcode), 8));
	}
	
	public void dumpALU() {
		System.out.println("=== ALU DUMP ===");
		System.out.println("Carry: " + carry);
		System.out.println("Zero: " + zero);
		System.out.println("Negative: " + negative);
		System.out.println("Parity: " + parityEven);
	}
	
	public static String padInt(int number, int length) {
		return String.format("%1$" + length + "s", "" + number).replace(' ', '0');
	}

	public static String padHexInt(int number, int length) {
		return String.format("%1$" + length + "s", "" + Integer.toHexString(number).toUpperCase()).replace(' ', '0');
	}

	public static String padBinaryInt(int number, int length) {
		return String.format("%1$" + length + "s", "" + Integer.toBinaryString(number & 0xFF).toUpperCase()).replace(' ', '0');
	}
	
	public static int isolateNumber(int bite, int offset, int bitAmount) {
		int mask = 0;
		mask |= 1;
		for (int i = 1; i < bitAmount; i++) {
			mask <<= 1;
			mask |= 1;
		}
		mask <<= offset;
		return (bite & (mask)) >> offset;
	}
	
	public static int isolateNumber(byte bite, int offset, int bitAmount) {
		int mask = 0;
		mask |= 1;
		for (int i = 1; i < bitAmount; i++) {
			mask <<= 1;
			mask |= 1;
		}
		mask <<= offset;
		return (bite & (mask)) >> offset;
	}
	
	public boolean isHalted() {
		return halted;
	}
	
	public byte[] getRegisters() {
		return registers;
	}
}