package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.tree.TreeNode;

public class RootCondition extends Condition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 583781564999853854L;

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
			return lua +"true";
		}
		return lua;
	}

}

