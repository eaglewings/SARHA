package ch.fhnw.students.keller.benjamin.sarha.fsm;

import java.util.ArrayList;

import ch.fhnw.students.keller.benjamin.sarha.LuaParseable;

public class State extends ArrayList<Transition> implements LuaParseable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stateName;
	public final StateMachine stateMachine;

	public State(StateMachine stateMachine, String stateName) {
		super();
		setStateName(stateName);
		this.stateMachine=stateMachine;
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof State) {
			return super.equals(o)
					&& ((State) o).getStateName().equals(stateName);
		}
		return false;

	}

	@Override
	public String parse() {
		int size = this.size();
		String lua ="";
		
		lua = "function "+stateMachine.getLuaFunctionForState(this) + "\n";
		lua += "\tcoroutine.yield()\n";
		if(size > 0){
		lua += "\tif ";
		
		for (int i=0;i<size-1;i++) {
			lua += this.get(i).parse();
			
			lua+="\telseif ";
		}
		lua += this.get(size-1).parse();
		lua += "\telse\n";
		
		lua += "\t\treturn "+stateMachine.getLuaFunctionForState(this)+"\n";
		lua +="\tend\n";
		}
		lua+= "end\n\n";
		return lua;
	}

}
