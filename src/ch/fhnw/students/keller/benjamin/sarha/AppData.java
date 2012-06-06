package ch.fhnw.students.keller.benjamin.sarha;

import java.util.ArrayList;

import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.DigitalIn;
import ch.fhnw.students.keller.benjamin.sarha.config.DigitalOut;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

 public class AppData {

	public static StateMachine stateMachine = getDummyStateMachine();
	public static ArrayList<Config> configs = getDummyConfigs();
	
	private static StateMachine getDummyStateMachine() {
		stateMachine = new StateMachine();
		stateMachine.add(new State(stateMachine, "State1"));
		stateMachine.add(new State(stateMachine, "State2"));
		for (State state : stateMachine) {
			state.add(new Transition(state, "t1"));
			state.add(new Transition(state, "t2"));
		}
		stateMachine.add(new State(stateMachine, "State3"));
		stateMachine.add(new State(stateMachine, "State4"));
		stateMachine.add(new State(stateMachine, "State5"));
		return stateMachine;
	}
	
	private static ArrayList<Config> getDummyConfigs(){
	configs = new ArrayList<Config>();
	configs.add(new Config("dummyconfig"));
	configs.get(0).add(new DigitalOut());
	configs.get(0).add(new DigitalOut());
	configs.get(0).add(new DigitalIn());
	return configs;
	}
}
