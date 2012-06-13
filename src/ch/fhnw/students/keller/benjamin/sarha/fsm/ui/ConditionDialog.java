package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.AnalogComperator;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.AnalogIn;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.DigitalIn;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;
import ch.fhnw.students.keller.benjamin.sarha.fsm.AnalogInCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.DigitalInCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.OperationCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.OperationCondition.OperationType;

public class ConditionDialog extends DialogFragment {
	private Button btOk, btCancel;
	private Context context;
	private Config config = IO.defaultConfig();
	private Condition condition;
	private SeekBar sbValue;
	private CheckBox cbInverted;
	private Spinner spIo;
	private TextView tvValue;
	private RadioButton rbGreaterThan, rbLessThan;
	private boolean newCondition;
	private OnSeekBarChangeListener sbChangeListener = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			tvValue.setText("" + progress);

		}
	};

	public ConditionDialog(Context context, Condition condition,
			boolean newCondition) {
		super();
		this.condition = condition;
		this.context = context;
		config = IO.defaultConfig();
		this.newCondition = newCondition;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, 0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);

		System.out.println("onCreateView: " + Condition.getType(condition));
		View v = null;
		switch (Condition.getType(condition)) {
		case OPERATION:
			v = inflater.inflate(R.layout.fsm_dialog_condition_operation, container,
					false);
			spIo = (Spinner) v.findViewById(R.id.spIo);
			spIo.setAdapter(new ConditionOperationDialogSpinnerAdapter());
			break;
		case AI:
			v = inflater.inflate(R.layout.fsm_dialog_condition_analog, container,
					false);
			sbValue = (SeekBar) v.findViewById(R.id.sbValue);
			sbValue.setOnSeekBarChangeListener(sbChangeListener);
			rbGreaterThan = (RadioButton) v.findViewById(R.id.rbGreaterThan);
			rbLessThan = (RadioButton) v.findViewById(R.id.rbLessThan);
			tvValue = (TextView) v.findViewById(R.id.tvValue);
			rbGreaterThan.setChecked(true);
			spIo = (Spinner) v.findViewById(R.id.spIo);
			spIo.setAdapter(new IoDialogSpinnerAdapter(context, config, IO.Type.AI));
			tvValue.setText("" + sbValue.getProgress());
			break;
		case DI:
			v = inflater.inflate(R.layout.fsm_dialog_condition_digital, container,
					false);
			spIo = (Spinner) v.findViewById(R.id.spIo);
			spIo.setAdapter(new IoDialogSpinnerAdapter(context, config, IO.Type.DI));
			break;
		default:
			break;
		}
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		cbInverted = (CheckBox) v.findViewById(R.id.cbInverted);
		
		btOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setValues();
				if (newCondition) {
					condition.getTree().addNode(condition);
				}
				ConditionDialog.this.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConditionDialog.this.dismiss();
			}
		});

		String title = "";
		if (newCondition) {
			switch (Condition.getType(condition)) {
			case OPERATION:
				title = "New Condition Operation";
				break;
			case AI:
				title = "New Analog Input Condition";				
				break;
			case DI:
				title = "New Digital Input Condition";
				break;
			default:
				break;
			}
		} else {
			if (condition.isInverted()) {
				cbInverted.setChecked(true);
			} else {
				cbInverted.setChecked(false);
			}
			switch (Condition.getType(condition)) {
			case OPERATION:
				title = "Edit Condition Operation";
				OperationCondition opc = (OperationCondition) condition;
				spIo.setSelection(((ConditionOperationDialogSpinnerAdapter) spIo
						.getAdapter()).getPosition(opc.getType()));
				break;
			case AI:
				AnalogInCondition aic = (AnalogInCondition) condition;
				title = "Edit Analog Input Condition";
				if (aic.getAc() == AnalogComperator.GREATER_THAN) {
					rbGreaterThan.setChecked(true);
				} else {
					rbLessThan.setChecked(true);
				}
				sbValue.setProgress(aic.getValue());
				tvValue.setText(""+aic.getValue());
				spIo.setSelection(((IoDialogSpinnerAdapter) spIo
						.getAdapter()).getPosition(aic.getAi()));
				break;
			case DI:
				title = "Edit Digital Input Condition";
				DigitalInCondition dic = (DigitalInCondition) condition;
				spIo.setSelection(((IoDialogSpinnerAdapter) spIo
						.getAdapter()).getPosition(dic.getDi()));
				break;
			default:
				break;
			}
		}
		getDialog().setTitle(title);
		return v;
	}

	protected void setValues() {
		if(cbInverted.isChecked()){
			condition.setInverted(true);
		}
		else{
			condition.setInverted(false);
		}
		switch (Condition.getType(condition)) {
		case OPERATION:
			OperationCondition opc = (OperationCondition) condition;
			opc.setType((OperationType) spIo.getSelectedItem());
			condition = opc;
			break;
		case AI:
			AnalogInCondition aic = (AnalogInCondition) condition;
			if (rbGreaterThan.isChecked()) {
				aic.setAc(AnalogComperator.GREATER_THAN);
			} else {
				aic.setAc(AnalogComperator.SMALLER_THAN);
			}
			aic.setValue(sbValue.getProgress());
			aic.setAi((AnalogIn) ((IOs) spIo.getSelectedItem()).address);
			condition = aic;
			break;
		case DI:
			DigitalInCondition dic = (DigitalInCondition) condition;
			dic.setDi((DigitalIn) ((IOs) spIo.getSelectedItem()).address);
			condition = dic;
			break;
		default:
			break;
		}
		condition.view.update(null, null);
	}

	
	class ConditionOperationDialogSpinnerAdapter extends BaseAdapter {
		OperationType[] types = OperationCondition.OperationType.values();
		@Override
		public int getCount() {
			return types.length;
		}

		@Override
		public Object getItem(int position) {
			try{
				return types[position];
			}
			catch (Exception e) {
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				v = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(android.R.layout.simple_spinner_item, null);
			} else {
				v = convertView;
			}
			TextView tv = (TextView) v.findViewById(android.R.id.text1);
			tv.setText(types[position].name());
			return v;
		}
		public int getPosition(OperationType type){
			return type.ordinal();
		}

	}
	
}
