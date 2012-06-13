package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;

public abstract class Action implements LuaParseable {
	
	public enum ActionType{
		DO,AO;
	}
	
	private AddressIdentifier ai;
	private int value=0;;
	
	
	public static ActionType getType(Action action){
		if(action instanceof DigitalOutAction){
			return ActionType.DO;
		}
		else{
			return ActionType.AO;
		}
	}



	public void setAddressIdentifier(AddressIdentifier ai) {
		this.ai = ai;
	}



	public AddressIdentifier getAddressIdentifier() {
		return ai;
	}



	public void setValue(int value) {
		this.value = value;
	}



	public int getValue() {
		return value;
	}

}
