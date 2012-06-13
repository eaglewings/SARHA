package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition.ConditionTypes;
import ch.fhnw.students.keller.benjamin.tree.Tree;
import ch.fhnw.students.keller.benjamin.tree.TreeNode;

public class ConditionTree extends Tree implements LuaParseable {

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

	class RootCondition extends Condition {

		public RootCondition(ConditionTree tree) {
			super(tree);
			setChildrenAllowed(1);
		}

		@Override
		public String parse() {
			String lua = "";
			for (TreeNode node : getChildNodes()) {
				lua += ((LuaParseable) node).parse();
			}
			if(lua.equals("")){
				return lua +"false";
			}
			return lua;
		}

	}

	@Override
	public String parse() {
		return ((LuaParseable) rootNode).parse();
	}
}
