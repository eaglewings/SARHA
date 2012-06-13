package ch.fhnw.students.keller.benjamin.sarha.config;


public class AnalogOut extends IOs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8332506169270835523L;

	public AnalogOut() {
		type=IO.Type.AO;
	}
	
	public String scale="";
	public String unit="";
	
	@Override
	public boolean equals(Object o){
		if( o instanceof AnalogOut){
			AnalogOut ao= (AnalogOut)o;
			if(super.equals(ao)&& ao.scale.equals(scale)&&ao.unit.equals(unit)){
				return true;			
			}
		}
		return false;
	}
}
