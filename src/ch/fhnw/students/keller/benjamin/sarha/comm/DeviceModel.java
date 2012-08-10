package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.util.Observable;

import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class DeviceModel extends Observable {
	private Config config;
	private boolean programRunning = false, debugActive = false;

	public DeviceModel(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void programRun() {
		programRunning = true;
		if (CommManager.protocol != null) {
			CommManager.protocol.programRun();
		}
		
		setChanged();
		notifyObservers();
	}

	public void programStop() {
		programRunning = false;
		if (CommManager.protocol != null) {
			CommManager.protocol.programStop();
		}
		setChanged();
		notifyObservers();
	}

	public boolean isProgramRunning() {
		return programRunning;
	}

	public void debugActive() {
		debugActive = true;
		if (CommManager.protocol != null) {
			CommManager.protocol.setDebug(true);
		}
		for (AddressIdentifier address : config.getUpdateAddresses()) {
			config.getIO(address).real();
		}
		setChanged();
		notifyObservers();
	}

	public void debugInactive() {
		debugActive = false;
		if (CommManager.protocol != null) {
			CommManager.protocol.setDebug(false);
		}
		setChanged();
		notifyObservers();
	}

	public boolean isDebug() {
		return debugActive;
	}

	public void setIOvalue(IOs io, int value) {
		io.setValue(value);
		io.notifyObservers();
	}

	public void setIOvalue(AddressIdentifier ai, int value) {
		IOs io = config.getIO(ai);
		if (io != null) {
			setIOvalue(io, value);
		}
		io.notifyObservers();
	}

	public void setIOoverrideValue(IOs io, int value) {
		io.setOverrideValue(value);
		io.notifyObservers();
	}

	public void setIOreal(IOs io) {
		io.real();
		io.notifyObservers();
	}

	public void setIOoverride(IOs io) {
		io.override();
		io.notifyObservers();
	}
}
