package de.schoko.intel8008emulator;

public enum Condition {
	NC(0),
	NZ(1),
	P(2),
	PO(3),
	C(4),
	Z(5),
	M(6),
	PE(7);
	
	static {
		NC.opposite = C;
		C.opposite = NC;
		NZ.opposite = Z;
		Z.opposite = NZ;
		P.opposite = M;
		M.opposite = P;
		PO.opposite = PE;
		PE.opposite = PO;
	}
	
	private int condition;
	private Condition opposite;
	
	private Condition(int condition) {
		this.condition = condition;
	}
	
	public int getCondition() {
		return condition;
	}
	
	public Condition getOpposite() {
		return opposite;
	}
}
