package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.util.Observable;

import android.content.Context;
import ch.fhnw.students.keller.benjamin.sarha.fsm.AnalogInCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.DigitalInCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.OperationCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.OperationCondition.OperationType;
import ch.fhnw.students.keller.benjamin.tree.NodeView;

public class ConditionView extends NodeView {
	public ConditionView(Context context, Condition node) {
		super(context, node);
	}
	 @Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		String str="";
		System.out.println("update: "+node);
		System.out.println("ConditionView ConditioType: "+Condition.getType((Condition) node));
		switch (Condition.getType((Condition) node)) {
		
		case OPERATION:
			OperationType type =((OperationCondition) node).getType();
			switch (type) {
			case OR:
				str="OR";
				break;
			case AND:
				str="AND";
				break;
			default:
				break;
			}
			
			
			break;
		case AI:
			
			str= ((AnalogInCondition)node).getAi().name();
			
			break;
		case DI:
			str=((DigitalInCondition)node).getDi().name();
			
			break;

		default:
			break;
		}
		if(((Condition) node).isInverted()){
			str="(Inverted) "+str;
		}
		tv1.setText(str);
		tv2.setText(((Condition) node).parse());
	}
}
