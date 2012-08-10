package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.Timer;

public class TimerAction extends Action {

	@Override
	public String parse() {
		return ((Timer) getAddressIdentifier()).getLuaVar()+" = tmr.start("+ getAddressIdentifier() +")\n";
	}

}
