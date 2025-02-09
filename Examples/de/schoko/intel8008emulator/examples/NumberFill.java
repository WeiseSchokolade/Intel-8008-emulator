package de.schoko.intel8008emulator.examples;

import static de.schoko.intel8008emulator.RegisterLocation.*;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryExecutor;

public class NumberFill {

	public static void main(String[] args) {
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(
				BIN.MVI(H),
				BIN.VAL(0),
				BIN.MVI(L),
				BIN.VAL(20),
				BIN.MVI(A),
				BIN.VAL(5),
				BIN.CPI(),
				BIN.VAL(0),
				BIN.JZ(),
				BIN.VAL(0),
				BIN.VAL(40),
				BIN.MOV(M, A),
				BIN.SUI(),
				BIN.VAL(1),
				BIN.INR(L),
				BIN.JMP(),
				BIN.VAL(6),
				BIN.VAL(0),
				BIN.VAL(-1)
				);
		int steps = 0;
		int overflow = 10000;
		while (!executor.isHalted() && steps < overflow) {
			try {
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
		executor.dump(32);
		executor.dumpRegisters();
	}

}
