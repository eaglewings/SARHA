package ch.fhnw.students.keller.benjamin.sarha.fsm;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.Timer;

public class TimerCondition extends Condition {
	/**
	 * 
	 */
	private static final long serialVersionUID = -869975596736736728L;
	private Timer tmr = Timer.T0;
	private int hSec=0, sec=0, min=0, h=0, d=0;
	
	public TimerCondition(Condition condition){
		super(0,condition);
	}
	public void setTmr(Timer tmr) {
		this.tmr = tmr;
	}
	public Timer getTmr() {
		return tmr;
	}
	
	
	
	@Override
	public String parse() {
		String lua = "";
		
		if(tmr!=null){
			lua= "(tmr.getdiffnow( "+tmr+", "+ tmr.getLuaVar()+" ) >= "+1000*(10*hSec +1000*(sec+(60*(min+60*(h+(24*d)))))) +")"; // Calc microseconds
		}
		if(isInverted()){
			lua="(not "+lua+")";
		}
		return lua;
		
	}
	public int getHsec() {
		return hSec;
	}
	public void setHsec(int hSec) {
		this.hSec = hSec;
	}

	public int getSec() {
		return sec;
	}
	public void setSec(int sec) {
		this.sec = sec;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public void setValues(int h, int min, int sec, int hSec){
		this.h=h;
		this.min=min;
		this.sec=sec;
		this.hSec=hSec;
	}
}
