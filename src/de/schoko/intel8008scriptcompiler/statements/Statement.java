package de.schoko.intel8008scriptcompiler.statements;

import de.schoko.intel8008scriptcompiler.GenerationContext;

public abstract class Statement {
	private short address;
	
	public abstract void generate(GenerationContext context);
	
	public void setAddress(short address) {
		this.address = address;
	}
	
	public short getAddress() {
		return address;
	}
}
