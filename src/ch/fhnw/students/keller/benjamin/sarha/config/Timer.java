package ch.fhnw.students.keller.benjamin.sarha.config;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.Type;

public class Timer extends IOs implements LuaParseable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1173962277260600715L;

	public Timer(){
		type=Type.TMR;
	}

	@Override
	public String parse() {
		return "local " +((IO.Timer)address).getLuaVar()+" = 0\n";
	}
	
}
