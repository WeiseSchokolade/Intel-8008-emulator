package de.schoko.intel8008emulator.examples;

import static de.schoko.intel8008emulator.RegisterLocation.A;
import static de.schoko.intel8008emulator.RegisterLocation.H;
import static de.schoko.intel8008emulator.RegisterLocation.L;
import static de.schoko.intel8008emulator.RegisterLocation.M;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.BinaryExecutor;

public class Addition {
	public static void main(String[] args) {
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(
				BIN.MVI(H),
				BIN.VAL(0),
				BIN.MVI(L),
				BIN.VAL(0xA),
				BIN.MVI(A),
				BIN.VAL(0x5),
				BIN.ADI(),
				BIN.VAL(0xD),
				BIN.MOV(M, A),
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
