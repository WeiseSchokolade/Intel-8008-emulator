package de.schoko.pseudoassembly;

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
	
	public static BinaryValue JMP() {
		return VAL(68);
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
	
	public static BinaryValue MOV(RegisterLocation from, RegisterLocation to) {
		return VAL((3 << 6) + (to.getLocation() << 3) + from.getLocation());
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
}
