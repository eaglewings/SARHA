package ch.fhnw.students.keller.benjamin.sarha.fsm;

import java.util.ArrayList;


public class Transition {
	private State toState;
	private State fromState;
	private String name;
	public ArrayList<Action> actions=new ArrayList<Action>();

	public Transition(State state, String name) {
		this.name = name;
		fromState = state;
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

	public String toString() {
		return name;
	}

	public State getFromState() {
		return fromState;
	}

}
