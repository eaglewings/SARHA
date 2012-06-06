package ch.fhnw.students.keller.benjamin.sarha.config;


public class AnalogIn extends IOs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1696640920357756137L;

	public AnalogIn() {
		type = IO.Type.AI;
	}

	public String scale="";
	public String unit="";

	public boolean equals(Object o) {
		if (o instanceof AnalogIn) {
			AnalogIn ai = (AnalogIn) o;
			if (super.equals(ai) && ai.scale.equals(scale)
					&& ai.unit.equals(unit)) {
				return true;
			}
		}
		return false;
	}
}
