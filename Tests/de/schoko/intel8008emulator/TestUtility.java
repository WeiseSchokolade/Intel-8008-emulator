package de.schoko.intel8008emulator;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtility {
	public static void runExecutor(BinaryExecutor executor) {
		runExecutor(executor, 10000);
	}
	
	public static void runExecutor(BinaryExecutor executor, int maxSteps) {
		while (!executor.isHalted() && maxSteps > 0) {
			executor.step();
			maxSteps--;
		}
		if (maxSteps <= 0) {
			fail("Test ran for too long!");
		}
	}
}
