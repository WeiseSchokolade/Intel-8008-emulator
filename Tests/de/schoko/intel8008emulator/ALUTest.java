package de.schoko.intel8008emulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ALUTest {
	 int performOperation(BinaryValue operation, BinaryValue a, BinaryValue b) {
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(
				BIN.MVI(RegisterLocation.A),
				a,
				BIN.MVI(RegisterLocation.B),
				b,
				operation,
				BIN.HLT()
				);
		TestUtility.runExecutor(executor);
		return executor.getRegisters()[0];
	}

	int performImmediateOperation(BinaryValue operation, BinaryValue a, BinaryValue b) {
		BinaryExecutor executor = new BinaryExecutor();
		executor.fillMemory(
				BIN.MVI(RegisterLocation.A),
				a,
				operation,
				b,
				BIN.HLT()
				);
		TestUtility.runExecutor(executor);
		return executor.getRegisters()[0];
	}
	
	@Test
	void testWhetherAdditionOfTwoLowPositiveIntegersWorks() {
		assertEquals(5, performOperation(
				BIN.ADD(RegisterLocation.B),
				BIN.VAL(2),
				BIN.VAL(3)
				));
		assertEquals(7, performOperation(
				BIN.ADD(RegisterLocation.B),
				BIN.VAL(4),
				BIN.VAL(3)
				));
		assertEquals(0, performOperation(
				BIN.ADD(RegisterLocation.B),
				BIN.VAL(0),
				BIN.VAL(0)
				));
	}
	
	@Test
	void testWhetherAdditionOfTwoLowIntegersWorks() {
		assertEquals(1, performOperation(
				BIN.ADD(RegisterLocation.B),
				BIN.VAL(-3),
				BIN.VAL(4)
				));
	}

	@Test
	void testWhetherAdditionOfAllIntegersWorks() {
		for (int a = -128; a < 128; a++) {
			for (int b = -128; b < 128; b++) {
				int added = a + b;
				if (added > 127) added -= 256;
				if (added < -128) added += 256;
				assertEquals(added, performOperation(
						BIN.ADD(RegisterLocation.B),
						BIN.VAL(a),
						BIN.VAL(b)
						));
				assertEquals(added, performImmediateOperation(
						BIN.ADI(),
						BIN.VAL(a),
						BIN.VAL(b)
						));
			}
		}
	}

	@Test
	void testWhetherImmediateAdditionOfTwoLowPositiveIntegersWorks() {
		assertEquals(5, performImmediateOperation(
				BIN.ADI(),
				BIN.VAL(2),
				BIN.VAL(3)
				));
		assertEquals(7, performImmediateOperation(
				BIN.ADI(),
				BIN.VAL(4),
				BIN.VAL(3)
				));
		assertEquals(0, performImmediateOperation(
				BIN.ADI(),
				BIN.VAL(0),
				BIN.VAL(0)
				));
	}
	
	@Test
	void testWhetherAdditionOfTwoLargeNumbersProperlyWraps() {
		assertEquals(-128, performOperation(
				BIN.ADD(RegisterLocation.B),
				BIN.VAL(64),
				BIN.VAL(64)
				));
		assertEquals(-2, performOperation(
				BIN.ADD(RegisterLocation.B),
				BIN.VAL(127),
				BIN.VAL(127)
				));
	}
	
	@Test
	void testWhetherSubtractionWorksProperly() {
		assertEquals(1, performOperation(
				BIN.SUB(RegisterLocation.B),
				BIN.VAL(3),
				BIN.VAL(2)
				));
		assertEquals(4, performOperation(
				BIN.SUB(RegisterLocation.B),
				BIN.VAL(7),
				BIN.VAL(3)
				));
		assertEquals(0, performOperation(
				BIN.SUB(RegisterLocation.B),
				BIN.VAL(0),
				BIN.VAL(0)
				));
		assertEquals(-1, performOperation(
				BIN.SUB(RegisterLocation.B),
				BIN.VAL(1),
				BIN.VAL(2)
				));
	}
	
	@Test
	void testWhetherSubtractionOfAllIntegersWorks() {
		for (int a = -128; a < 128; a++) {
			for (int b = -128; b < 128; b++) {
				int subbed = a - b;
				if (subbed > 127) subbed -= 256;
				if (subbed < -128) subbed += 256;
				assertEquals(subbed, performOperation(
						BIN.SUB(RegisterLocation.B),
						BIN.VAL(a),
						BIN.VAL(b)
						));
				assertEquals(subbed, performImmediateOperation(
						BIN.SUI(),
						BIN.VAL(a),
						BIN.VAL(b)
						));
			}
		}
	}

}
