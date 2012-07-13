package ch.fhnw.students.keller.benjamin.sarha.fsm;

import java.io.Serializable;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;

public abstract class Action implements Serializable, LuaParseable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8254924385374290677L;



	public enum ActionType{
		DO,AO;
	}
	
	private AddressIdentifier ai;
	private int value=0;
	
	
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
