package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.AnalogOut;

public class AnalogOutAction extends Action {
	private AnalogOut ao;
	private int value;
	
	public void setAo(AnalogOut ao) {
		this.ao = ao;
	}

	public AnalogOut getAo() {
		return ao;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return null;
	}
}
