package ch.fhnw.students.keller.benjamin.sarha.fsm;


public class AnalogOutAction extends Action {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8175387976992259075L;

	@Override
	public String parse() {
		return "kit.IO."+getAddressIdentifier()+".real="+getValue()+"\n";
	}
}
