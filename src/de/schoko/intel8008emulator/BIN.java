package de.schoko.intel8008emulator;

public final class BIN {
	public static BinaryValue VAL(byte b) {
		return new BinaryValue(b);
	}
	
	public static BinaryValue VAL(int i) {
		return new BinaryValue((byte) i);
	}
	
	public static BinaryValue HLT() {
		return VAL(0);
	}
	
	public static BinaryValue IN(int port) {
		return VAL(65 + (port << 1));
	}

	public static BinaryValue OUT(int port) {
		return VAL(65 + ((port + 8) << 1));
	}
	
	public static BinaryValue JMP() {
		return VAL(68);
	}

	public static BinaryValue JCC(Condition condition) {
		return VAL(64 + (condition.getCondition() << 3));
	}
	
	public static BinaryValue JNC() {
		return VAL(64);
	}
	
	public static BinaryValue JNZ() {
		return VAL(72);
	}

	public static BinaryValue JP() {
		return VAL(80);
	}

	public static BinaryValue JPO() {
		return VAL(88);
	}

	public static BinaryValue JC() {
		return VAL(96);
	}

	public static BinaryValue JZ() {
		return VAL(104);
	}
	
	public static BinaryValue JM() {
		return VAL(112);
	}

	public static BinaryValue JPE() {
		return VAL(120);
	}
	
	public static BinaryValue CALL() {
		return VAL(70);
	}

	public static BinaryValue CCC(Condition condition) {
		return VAL(64 + (condition.getCondition() << 3) + 2);
	}
	
	public static BinaryValue CNC() {
		return VAL(66);
	}

	public static BinaryValue CNZ() {
		return VAL(74);
	}

	public static BinaryValue CP() {
		return VAL(82);
	}
	
	public static BinaryValue CPO() {
		return VAL(90);
	}

	public static BinaryValue CC() {
		return VAL(98);
	}

	public static BinaryValue CZ() {
		return VAL(106);
	}

	public static BinaryValue CM() {
		return VAL(114);
	}
	
	public static BinaryValue CPE() {
		return VAL(122);
	}

	public static BinaryValue RET() {
		return VAL(7);
	}

	public static BinaryValue RCC(Condition condition) {
		return VAL((condition.getCondition() << 3) + 3);
	}
	
	public static BinaryValue RNC() {
		return VAL(3);
	}
	
	public static BinaryValue RNZ() {
		return VAL(11);
	}
	
	public static BinaryValue RP() {
		return VAL(19);
	}
	
	public static BinaryValue RPO() {
		return VAL(27);
	}
	
	public static BinaryValue RC() {
		return VAL(35);
	}
	
	public static BinaryValue RZ() {
		return VAL(43);
	}
	
	public static BinaryValue RM() {
		return VAL(51);
	}
	
	public static BinaryValue RPE() {
		return VAL(59);
	}

	public static BinaryValue RST(int location) {
		return VAL((location << 3) + 5);
	}
	
	public static BinaryValue MOV(RegisterLocation destination, RegisterLocation source) {
		return VAL((3 << 6) + (destination.getLocation() << 3) + source.getLocation());
	}
	
	public static BinaryValue MVI(RegisterLocation to) {
		return VAL((to.getLocation() << 3) + 6);
	}
	
	public static BinaryValue ADD(RegisterLocation from) {
		return VAL((2 << 6) + (0 << 3) + from.getLocation());
	}

	public static BinaryValue ADI() {
		return VAL(4);
	}
	
	public static BinaryValue ADC(RegisterLocation from) {
		return VAL((2 << 6) + (1 << 3) + from.getLocation());
	}

	public static BinaryValue ACI() {
		return VAL(12);
	}
	
	public static BinaryValue SUB(RegisterLocation from) {
		return VAL((2 << 6) + (2 << 3) + from.getLocation());
	}

	public static BinaryValue SUI() {
		return VAL(20);
	}
	
	public static BinaryValue SBB(RegisterLocation from) {
		return VAL((2 << 6) + (3 << 3) + from.getLocation());
	}

	public static BinaryValue SBI() {
		return VAL(28);
	}
	
	public static BinaryValue ANA(RegisterLocation from) {
		return VAL((2 << 6) + (4 << 3) + from.getLocation());
	}

	public static BinaryValue ANI() {
		return VAL(36);
	}
	
	public static BinaryValue XRA(RegisterLocation from) {
		return VAL((2 << 6) + (5 << 3) + from.getLocation());
	}

	public static BinaryValue XRI() {
		return VAL(44);
	}
	
	public static BinaryValue OR(RegisterLocation from) {
		return VAL((2 << 6) + (6 << 3) + from.getLocation());
	}

	public static BinaryValue ORI() {
		return VAL(52);
	}
	
	public static BinaryValue CMP(RegisterLocation from) {
		return VAL((2 << 6) + (7 << 3) + from.getLocation());
	}

	public static BinaryValue CPI() {
		return VAL(60);
	}
	
	public static BinaryValue INR(RegisterLocation register) {
		return VAL((register.getLocation() << 3));
	}

	public static BinaryValue DEC(RegisterLocation register) {
		return VAL((register.getLocation() << 3) + 1);
	}
	
	public static BinaryValue RLC() {
		return VAL(2);
	}
	
	public static BinaryValue RRC() {
		return VAL(10);
	}
	
	public static BinaryValue RAL() {
		return VAL(18);
	}
	
	public static BinaryValue RAR() {
		return VAL(26);
	}
}
