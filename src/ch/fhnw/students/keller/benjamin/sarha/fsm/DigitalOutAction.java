package ch.fhnw.students.keller.benjamin.sarha.fsm;


public class DigitalOutAction extends Action {

	@Override
	public String parse() {
		return "kit.IO."+getAddressIdentifier()+".real="+getValue()+"\n";
	}
}
