package ch.fhnw.students.keller.benjamin.sarha.fsm;


public class DigitalOutAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3057614920479386663L;

	@Override
	public String parse() {
		return "kit.IO."+getAddressIdentifier()+".real="+getValue()+"\n";
	}
}
