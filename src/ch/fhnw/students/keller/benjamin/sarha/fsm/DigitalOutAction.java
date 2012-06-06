package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.DigitalOut;

public class DigitalOutAction extends Action {
	private DigitalOut dio;
	private boolean value;
	
	public void setDo(DigitalOut dio) {
		this.dio = dio;
	}

	public DigitalOut getDo() {
		return dio;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return null;
	}
}
