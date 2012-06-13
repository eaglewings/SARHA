package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;


import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.AnalogOut;
import ch.fhnw.students.keller.benjamin.sarha.config.IO.DigitalOut;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Action;

public class ActionDialog extends DialogFragment {
	private Button btOk, btCancel;
	private Context context;
	private static Config config = IO.defaultConfig();
	private Action action;
	private SeekBar sbValue;
	private Spinner spIo;
	private TextView tvValue;
	private ToggleButton tbValue;
	private boolean newAction;
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

	public ActionDialog(Context context, Action action,
			boolean newAction) {
		super();
		this.action = action;
		this.context = context;
		config = IO.defaultConfig();
		this.newAction = newAction;
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
		View v = null;
		
		switch (Action.getType(action)) {
		case AO:
			v = inflater.inflate(R.layout.fsm_dialog_action_analog, container,
					false);
			sbValue = (SeekBar) v.findViewById(R.id.sbValue);
			sbValue.setOnSeekBarChangeListener(sbChangeListener);
			tvValue = (TextView) v.findViewById(R.id.tvValue);
			spIo = (Spinner) v.findViewById(R.id.spIo);
			spIo.setAdapter(new ActionDialogSpinnerAdapter(IO.Type.AO));
			tvValue.setText("" + sbValue.getProgress());
			break;
		case DO:
			v = inflater.inflate(R.layout.fsm_dialog_action_digital, container,
					false);
			spIo = (Spinner) v.findViewById(R.id.spIo);
			spIo.setAdapter(new ActionDialogSpinnerAdapter(IO.Type.DO));
			tbValue = (ToggleButton) v.findViewById(R.id.tbValue);
			break;
		default:
			break;
		}
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		
		btOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setValues();
				if (newAction) {
					AppData.currentWorkingTransition.addAction(action);
					System.out.println("added");
				}
				ActionDialog.this.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActionDialog.this.dismiss();
			}
		});

		String title = "";
		if (newAction) {
			switch (Action.getType(action)) {
			case AO:
				title = "New Analog Output Action";				
				
				break;
			case DO:
				title = "New Digital Output Action";
				
				break;
			default:
				break;
			}
			
		} else {
			switch (Action.getType(action)) {
			case AO:
				title = "Edit Analog Output Action";
				sbValue.setProgress(action.getValue());
				tvValue.setText(""+action.getValue());
				spIo.setSelection(((ActionDialogSpinnerAdapter) spIo
						.getAdapter()).getPosition(action.getAddressIdentifier()));
				break;
			case DO:
				title = "Edit Digital Output Action";
				tbValue.setChecked(action.getValue()>0?true:false);
				spIo.setSelection(((ActionDialogSpinnerAdapter) spIo
						.getAdapter()).getPosition(action.getAddressIdentifier()));
				break;
			default:
				break;
			}
		}
		
		getDialog().setTitle(title);
		return v;
		
	}
	
	protected void setValues() {
		switch (Action.getType(action)) {
		case AO:
			action.setValue(sbValue.getProgress());
			action.setAddressIdentifier((AnalogOut) ((IOs) spIo.getSelectedItem()).address);
			break;
		case DO:
			action.setValue(tbValue.isChecked()?1:0);
			action.setAddressIdentifier((DigitalOut) ((IOs) spIo.getSelectedItem()).address);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		((TransitionActivity) context).actionAdapter.notifyDataSetChanged();
		super.onDismiss(dialog);
	}

	class ActionDialogSpinnerAdapter extends BaseAdapter {
		private ArrayList<IOs> ios = new ArrayList<IOs>();

		public ActionDialogSpinnerAdapter(IO.Type type) {
			ios = ActionDialog.config.getListOfType(type);
		}

		@Override
		public int getCount() {
			return ios.size();
		}

		@Override
		public Object getItem(int position) {
			if (ios.get(position) != null) {
				return ios.get(position);
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
			tv.setText(((IOs) getItem(position)).name);
			return v;
		}

		public int getPosition(AddressIdentifier ai) {
			for (IOs io : ios) {
				if (io.address == ai) {
					return ios.indexOf(io);
				}
			}
			return -1;
		}

	}

	
	
}
