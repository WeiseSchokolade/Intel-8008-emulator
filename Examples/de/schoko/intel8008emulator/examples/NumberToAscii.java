package de.schoko.intel8008emulator.examples;

import static de.schoko.intel8008emulator.RegisterLocation.*;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryExecutor;

public class NumberToAscii {
	public static void main(String[] args) {
		// Subroutine to take a number and convert it into ascii
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(
				BIN.MVI(E), // 00
				BIN.VAL(0x27),
				BIN.CALL(),
				BIN.VAL(0x20),
				BIN.VAL(0x0),
				BIN.HLT(),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				// 10 Division subroutine, Input A, Divisor B, result C, module A
				BIN.MVI(C),
				BIN.VAL(0),
				BIN.CMP(B),
				BIN.RM(),
				BIN.SUB(B),
				BIN.INR(C),
				BIN.JMP(),
				BIN.VAL(0x12),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				BIN.VAL(0),
				// 20 // Subroutine: Takes unsigned number as input in E and writes it into 0x100
				BIN.MVI(B),
				BIN.VAL(0xA),
				BIN.MOV(A, E),
				BIN.CALL(),
				BIN.VAL(0x10),
				BIN.VAL(0),
				BIN.ADI(),
				BIN.VAL(0x30),
				BIN.MVI(H),
				BIN.VAL(0x1),
				BIN.MVI(L),
				BIN.VAL(0x2),
				BIN.MOV(M, A),
				BIN.MOV(A, C),
				BIN.CALL(),
				BIN.VAL(0x10),
				BIN.VAL(0), // 30
				BIN.ADI(),
				BIN.VAL(0x30),
				BIN.MVI(H),
				BIN.VAL(0x1),
				BIN.MVI(L),
				BIN.VAL(0x1),
				BIN.MOV(M, A),
				BIN.MVI(H),
				BIN.VAL(0x1),
				BIN.MVI(L),
				BIN.VAL(0x0),
				BIN.MOV(A, C),
				BIN.ADI(),
				BIN.VAL(0x30),
				BIN.MOV(M, A),
				BIN.RET()
				);
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
