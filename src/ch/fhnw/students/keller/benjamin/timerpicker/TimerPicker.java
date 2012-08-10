package ch.fhnw.students.keller.benjamin.timerpicker;

import ch.fhnw.students.keller.benjamin.sarha.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;

public class TimerPicker extends LinearLayout {
	private TableRow trPickers;
	private Picker pkH, pkMin, pkSec,pkHsec;
	public TimerPicker(Context context, AttributeSet attr) {
		super(context, attr);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.timerpicker_timerpicker, this);
		trPickers=(TableRow) findViewById(R.id.trPickers);
		pkH = new Picker(context, null, 0, 999, 1, 1);
		pkMin = new Picker(context, pkH, 0, 59, 1, 1);
		pkSec = new Picker(context, pkMin, 0, 59, 1, 1);
		pkHsec = new Picker(context, pkSec, 0, 99, 25, 1);
		trPickers.addView(pkH);
		trPickers.addView(pkMin);
		trPickers.addView(pkSec);
		trPickers.addView(pkHsec);
		
	}
	public int getH(){
		return pkH.getModel().getValue();
	}
	public void setH(int h){
		pkH.getModel().setValue(h);
	}

	public int getMin(){
		return pkMin.getModel().getValue();
	}
	public void setMin(int min){
		pkMin.getModel().setValue(min);
	}
	
	public int getSec(){
		return pkSec.getModel().getValue();
	}
	public void setSec(int sec){
		pkSec.getModel().setValue(sec);
	}
	public int getHsec(){
		return pkHsec.getModel().getValue();
	}
	public void setHsec(int hSec){
		pkHsec.getModel().setValue(hSec);
	}
	public void setValues(int h, int min, int sec, int hSec){
		setH(h);
		setMin(min);
		setSec(sec);
		setHsec(hSec);
		}

}
