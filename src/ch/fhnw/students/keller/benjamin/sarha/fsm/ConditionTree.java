package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition.ConditionTypes;
import ch.fhnw.students.keller.benjamin.tree.Tree;

public class ConditionTree extends Tree implements LuaParseable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4211415172002602236L;

	public ConditionTree() {
		rootNode = new RootCondition(this);

	}

	public void addNode(ConditionTypes type) {
		Condition condition;
		Condition parent = (Condition) getSelectedNode();
		if (parent == null) {
			parent = (Condition) rootNode;
		}
		if (parent.canHaveMoreChildren()) {
			switch (type) {
			case OPERATION:
				condition = new OperationCondition(parent);
				break;
			case AI:
				condition = new AnalogInCondition(parent);
				break;
			case DI:
				condition = new DigitalInCondition(parent);
				break;
			default:
				condition = new OperationCondition(parent);
			}

			addNode(condition);
		}
	}

	@Override
	public String parse() {
		return ((LuaParseable) rootNode).parse();
	}
}
