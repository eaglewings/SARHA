package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.DigitalIn;

public class DigitalInCondition extends Condition {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4156857153478703521L;
	private DigitalIn di=DigitalIn.DI0;
	public DigitalInCondition(Condition condition) {
		super(0, condition);
	}
	@Override
	public String parse() {
		String lua="";
		if(di!=null){
			lua= "(kit.IO."+di+".merge==0)";
		}
		if(isInverted()){
			lua="(not "+lua+")";
		}
		return lua;
	}
	public void setDi(DigitalIn di) {
		this.di = di;
	}
	public DigitalIn getDi() {
		return di;
	}

}
