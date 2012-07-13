package ch.fhnw.students.keller.benjamin.sarha.fsm;

import java.io.Serializable;
import java.util.ArrayList;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Action.ActionType;

public class Transition implements Serializable, LuaParseable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6458618589346480152L;
	private State toState;
	private State fromState;
	private String name;
	public ConditionTree condition = new ConditionTree();
	public ArrayList<ArrayList<Action>> actions = new ArrayList<ArrayList<Action>>();

	public Transition(State state, String name) {
		this.name = name;
		fromState = state;
		for (int i = 0; i < ActionType.values().length; i++) {
			actions.add(new ArrayList<Action>());
		}
	}

	public State getToState() {
		return toState;
	}

	public void setToState(State toState) {
		this.toState = toState;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public State getFromState() {
		return fromState;
	}
	public void addAction(Action action){
		actions.get(Action.getType(action).ordinal()).add(action);
	}
	public void removeAction(Action action){
		actions.get(Action.getType(action).ordinal()).remove(action);
	}

	@Override
	public String parse() {
		String lua = "";
		lua += condition.parse() + " then\n";
		for (ArrayList<Action> typeList : actions) {

			for (Action action : typeList) {
				lua += "\t\t\t"+action.parse();
			}
		}
		lua += "\t\t\treturn " + toState.stateMachine.getLuaFunctionForState(toState)+ "\n";
		return lua;
	}

}
