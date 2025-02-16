package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008emulator.BinaryExecutor;
import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008scriptcompiler.statements.Statement;

public class ScriptCompilerMain {
	public static void main(String[] args) {
		FrontEnd compiler = new FrontEnd();
		List<Token> tokens = compiler.tokenize("""
				var a = 3;
				if (a == (2 + 1)) {
					a = 5;
				}
				output a;
				""");
		List<Statement> statements = compiler.parseStatements(tokens, List.of());
		System.out.println(statements);
		MemoryGenerator generator = new MemoryGenerator();
		List<BinaryValue> assembled = generator.generate(statements);
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(assembled.toArray(new BinaryValue[0]));
		executor.setOutputListener((value, port) -> {
			System.out.println("[Port " + port + "] " + BinaryExecutor.padHexInt(value, 2));
		});
		int steps = 0;
		int overflow = 1000;
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
		executor.dumpRegisters();
		executor.dumpALU();
	}
}
