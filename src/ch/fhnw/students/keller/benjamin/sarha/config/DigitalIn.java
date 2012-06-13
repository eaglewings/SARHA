package ch.fhnw.students.keller.benjamin.sarha.config;


public class DigitalIn  extends IOs{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -890308537492882318L;

	public DigitalIn() {
		type=IO.Type.DI;
	}

	@Override
	public boolean equals(Object o){
		if( o instanceof DigitalIn){
			DigitalIn di= (DigitalIn)o;
			if(super.equals(di)){
				return true;			
			}
		}
		return false;
	}
	
}
