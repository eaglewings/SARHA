package ch.fhnw.students.keller.benjamin.sarha.config;


public class DigitalOut extends IOs{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8065957265296022710L;



	public DigitalOut() {
		type=IO.Type.DO;
	}

	
	
	public boolean equals(Object o){
		if( o instanceof DigitalOut){
			DigitalOut dio= (DigitalOut)o;
			if(super.equals(dio)){
				return true;			
			}
		}
		return false;
	}
}
