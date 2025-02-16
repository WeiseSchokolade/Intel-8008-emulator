package de.schoko.intel8008emulator;

public interface OutputListener {
	public void handleOutput(byte value, int port);
}
