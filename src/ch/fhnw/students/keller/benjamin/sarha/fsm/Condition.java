package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.tree.TreeNode;

public abstract class Condition extends TreeNode implements LuaParseable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4041059450656321373L;
	@Override
	public abstract String parse();
	
	public enum ConditionTypes{
		OPERATION, DI,AI;
	}
	private boolean inverted;
	public Condition(int childrenAllowed, Condition condition){
		super(childrenAllowed, condition);
		
	}
	public Condition(ConditionTree tree){
		super(tree);
	}
	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}
	public boolean isInverted() {
		return inverted;
	}
	
	public static ConditionTypes getType(Condition condition){
		if(condition instanceof OperationCondition){
			return ConditionTypes.OPERATION;
		}
		else if(condition instanceof DigitalInCondition){
			return ConditionTypes.DI;
		}
		else{
			return ConditionTypes.AI;
		}
	}
	

}
