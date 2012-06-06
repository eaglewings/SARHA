package ch.fhnw.students.keller.benjamin.sarha.fsm;

import java.util.Vector;

public class StateMachine extends Vector<State> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private State initialState;

	public StateMachine() {
		super();
		name = "NewStateMachine";

	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getInitialState() {
		return initialState;
	}

	public void printMachine() {
		String str = "State Machine: " + name;
		for (State states : this) {
			str = str + "\n State: " + states;
			for (Transition transitions : states) {
				str = str + "\n  Transition: " + transitions;
			}
		}
		System.out.println(str);
	}

	@Override
	public String toString() {

		return name;
	}

	public void setStateName(State state, String name) {
		for (State states : this) {
			if (states.getStateName().equals(name)) {
				return;
			}
			if (!(name.equals("") || name == null)) {
				state.setStateName(name);
			}

		}
	}

}
