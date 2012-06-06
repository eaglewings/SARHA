package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;

public abstract class Action implements LuaParseable {
	
	public enum ActionType{
		DO,AO;
	}
	
	public static ActionType getType(Action action){
		if(action instanceof DigitalOutAction){
			return ActionType.DO;
		}
		else{
			return ActionType.AO;
		}
	}

}
