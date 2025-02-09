package de.schoko.intel8008emulator;

public enum RegisterLocation {
	A(0),
	B(1),
	C(2),
	D(3),
	E(4),
	H(5),
	L(6),
	M(7);
	
	private int location;

	private RegisterLocation(int location) {
		this.location = location;
	}
	
	public int getLocation() {
		return location;
	}
}
