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
		config.getAssignedAddresses();
	}

	public Config getConfig() {
		return config;
	}

	public void programRun() {
		programRunning = true;
		setChanged();
		notifyObservers();
	}

	public void programStop() {
		programRunning = false;
		setChanged();
		notifyObservers();
	}

	public boolean isProgramRunning() {
		return programRunning;
	}

	public void debugActive() {
		debugActive = true;
		setChanged();
		notifyObservers();
	}

	public void debugInactive() {
		debugActive = false;
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
	public void setIOvalue(AddressIdentifier ai, int value){
		IOs io=config.getIO(ai);
		if( io!=null){
			setIOvalue(io,value);
		}
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
