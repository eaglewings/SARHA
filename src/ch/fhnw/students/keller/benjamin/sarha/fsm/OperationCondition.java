package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.ConditionView;

public class OperationCondition extends Condition {
	public static enum OperationType {
		AND, OR
	}

	private OperationType type=OperationType.AND;

	public OperationCondition(Condition condition) {
		super(INFINITE_CHILDREN, condition);
		view = new ConditionView(getTree().view.getContext(), this);
		addObserver(view);
	}

	@Override
	public String parse() {
		int size = getChildNodes().size();
		if (size == 0) {
			return "";
		}
		String lua = "(";
		String childParse = "";
		for (int i = 0; i < size - 1; i++) {
			childParse = ((LuaParseable) getChildNodes().get(i)).parse();
			if (!childParse.equals("")) {
				lua += childParse;
				if (type == OperationType.AND) {
					lua += " and ";
				} else {
					lua += " or ";
				}
			}
		}
		lua += ((LuaParseable) getChildNodes().get(size - 1)).parse();
		lua += ")";
		if (isInverted() && !lua.equals("()")) {
			lua = "(not " + lua + ")";
		}
		return lua;

	}


	public void setType(OperationType type) {
		this.type = type;
	}

	public OperationType getType() {
		return type;
	}

}
