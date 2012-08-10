package ch.fhnw.students.keller.benjamin.timerpicker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Observable;
import java.util.Observer;

import ch.fhnw.students.keller.benjamin.sarha.R;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Picker extends LinearLayout implements Observer{
	private ImageView ivUp, ivDown;
	private EditText etValue;
	private PickerDataModel model;

	public Picker(Context context, Picker higherPicker, int min, int max, int inc, int div) {
		super(context);
		View.inflate(context, R.layout.timerpicker_picker, this);
		model = new PickerDataModel(this,higherPicker, min, max, inc, div);
		ivUp = (ImageView) findViewById(R.id.ivUp);
		ivDown = (ImageView) findViewById(R.id.ivDown);
		etValue = (EditText) findViewById(R.id.etValue);
		ivUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				model.valueUp();
			}
		});
		ivDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				model.valueDown();
			}
		});
		etValue.setSingleLine();
		etValue.setCursorVisible(false);
		etValue.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				System.out.println("action");
				model.setValue(Integer.parseInt(v.getText().toString()));
				etValue.setCursorVisible(false);
				return false;
			}
		});
		etValue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etValue.setCursorVisible(true);
				etValue.selectAll();
				
			}
		});
		etValue.setText(formattedValue());
	}
	public PickerDataModel getModel(){
		return model;
	}
	

	private CharSequence formattedValue() {
		int value = model.getValue();
		int div = model.getDiv();
		System.out.println("formattedValue() " + model.getValue());
		long print;
		if(div!=0){
		print = value/div;
		}
		else{
			print= value;
		}
		if (div == 1 || div == 0){
			return new DecimalFormat("#").format(print);
		}
		else{
			return new DecimalFormat(".##").format(print);
		}
	}



	@Override
	public void update(Observable arg0, Object arg1) {
		etValue.setText(formattedValue());
		
	}

	

}
