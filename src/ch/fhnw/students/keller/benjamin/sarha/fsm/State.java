package ch.fhnw.students.keller.benjamin.sarha.fsm;

import java.util.Vector;

public class State extends Vector<Transition> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stateName;
	private boolean active; // Flag for active State

	public State(StateMachine stateMachine, String stateName) {
		super();
		setStateName(stateName);
	}

	public void activate() {
		active = true;
	}

	public void deactivate() {
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	public void setStateName(String str) {
		stateName = str;
	}

	public String getStateName() {
		return stateName;
	}

	@Override
	public String toString() {
		if (stateName != null) {
			return stateName;
		} else {
			return super.toString();
		}
	}

	public boolean equals(Object o) {
		if (o instanceof State) {
			return super.equals(o)
					&& ((State) o).getStateName().equals(stateName);
		}
		return false;

	}

}
