package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.DigitalIn;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.ConditionView;

public class DigitalInCondition extends Condition {
	private DigitalIn di=DigitalIn.DI0;
	public DigitalInCondition(Condition condition) {
		super(0, condition);
		view = new ConditionView(getTree().view.getContext(), this);
		addObserver(view);
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
