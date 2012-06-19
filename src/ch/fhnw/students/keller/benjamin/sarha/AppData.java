package ch.fhnw.students.keller.benjamin.sarha;

import java.util.ArrayList;

import ch.fhnw.students.keller.benjamin.sarha.comm.Device;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.DigitalIn;
import ch.fhnw.students.keller.benjamin.sarha.config.DigitalOut;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

 public class AppData {

	public static StateMachine stateMachine = getDummyStateMachine();
	public static ArrayList<Config> configs = getDummyConfigs();
	public static StateMachine currentWorkingStateMachine;
	public static State currentWorkingState;
	public static Transition currentWorkingTransition;
	public static ArrayList<StateMachine> stateMachines = new ArrayList<StateMachine>();
	public static ArrayList<Device> devices;
	static {
		stateMachines.add(getDummyStateMachine());
	}
	
	private static StateMachine getDummyStateMachine() {
		stateMachine = new StateMachine();
		stateMachine.add(new State(stateMachine, "State1"));
		stateMachine.add(new State(stateMachine, "State2"));
		stateMachine.add(new State(stateMachine, "State3"));
		stateMachine.add(new State(stateMachine, "State4"));
		stateMachine.add(new State(stateMachine, "State5"));
		for (State state : stateMachine.subList(0, 1)) {
			Transition t1= new Transition(state, "t1");
			Transition t2 = new Transition(state, "t2");
			t1.setToState(stateMachine.get(stateMachine.indexOf(state)+1));
			t2.setToState(stateMachine.get(stateMachine.indexOf(state)+2));
			state.add(t1);
			state.add(t2);
		}
		stateMachine.setInitialState(stateMachine.get(0));
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
