package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.AnalogIn;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.AnalogComperator;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.ConditionView;

public class AnalogInCondition extends Condition {
	private AnalogIn ai=AnalogIn.AI0;
	private AnalogComperator ac=AnalogComperator.GREATER_THAN;
	private int value=50;
	public AnalogInCondition(Condition condition) {
		super(0, condition);
		view = new ConditionView(getTree().view.getContext(), this);
		addObserver(view);
	}

	@Override
	public String parse() {
		String lua="";
		if(ai!=null && ac!=null){
			lua= "("+ai+" "+ac.getString()+" "+value+")";
		}
		if(isInverted()){
			lua="(not "+lua+")";
		}
		return lua;
	}

	public void setAi(AnalogIn ai) {
		this.ai = ai;
	}

	public AnalogIn getAi() {
		return ai;
	}

	public void setAc(AnalogComperator ac) {
		this.ac = ac;
	}

	public AnalogComperator getAc() {
		return ac;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}