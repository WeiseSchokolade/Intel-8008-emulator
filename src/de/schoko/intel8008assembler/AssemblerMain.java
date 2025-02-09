package de.schoko.intel8008assembler;

import java.util.List;

import de.schoko.intel8008emulator.BinaryExecutor;
import de.schoko.intel8008emulator.BinaryValue;

public class AssemblerMain {
	public static void main(String[] args) {
		String program = """
				MVI E, 0x27h
				CALL write
				HLT
				
				division:
				MVI C, 0x0h
				divisionLoop:
				CMP B
				RM
				SUB B
				INR C
				JMP divisionLoop
				
				write:
				MVI B, 0xAh
				MOV A, E
				CALL division
				ADI 0x30h
				MVI H, 0x1h
				MVI L, 0x2h
				MOV M, A
				MOV A, C
				CALL division
				ADI 0x30h
				MVI H, 0x1h
				MVI L, 0x1h
				MOV M, A
				MVI H, 0x1h
				MVI L, 0x0h
				MOV A, C
				ADI 0x30h
				MOV M, A
				RET
				""";
		Assembler assembler = new Assembler();
		List<BinaryValue> assembled = assembler.assemble(program);
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(assembled.toArray(new BinaryValue[0]));
		int steps = 0;
		int overflow = 10000;
		while (!executor.isHalted() && steps < overflow) {
			try {
				System.out.print("Step: " + steps + " - ");
				executor.step();
			} catch (Exception e) {
				executor.dump(32);
				e.printStackTrace();
				return;
			}
			steps++;
		}
		if (!executor.isHalted()) {
			System.out.println("Overflow occured!");
		} else {
			System.out.println("Steps: " + steps);
		}
		executor.dump(64);
		executor.dump(0x100, 4);
		executor.dumpRegisters();
		executor.dumpALU();
	}
	
}
