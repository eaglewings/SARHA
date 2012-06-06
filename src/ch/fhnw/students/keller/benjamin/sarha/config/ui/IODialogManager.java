package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.Arrays;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.AnalogIn;
import ch.fhnw.students.keller.benjamin.sarha.config.AnalogOut;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class IODialogManager extends DialogFragment {
	private IOs io;
	private boolean newIO;
	private EditText nameEdit, scaleEdit, unitEdit;
	private Spinner hwAddress;
	private Button okButton, cancelButton;
	private Context context;
	private Config config;

	public IODialogManager() {
		super();
	}

	public IODialogManager(Context context,IOs item, boolean newIO) {
		super();
		this.io = item;
		this.newIO = newIO;
		this.context = context;
		config = ((ConfigActivity) context).config;

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

		String title = new String();
		if (newIO) {
			title = "New " + IO.typeDescriptors.get(io.type);
		} else {
			title = "Edit " + IO.typeDescriptors.get(io.type);
		}

		View v = null;
		switch (io.type) {
		case AI:
			v = inflater.inflate(R.layout.config_dialog_analog, container, false);
			break;
		case AO:
			v = inflater.inflate(R.layout.config_dialog_analog, container, false);
			break;
		case DI:
			v = inflater.inflate(R.layout.config_dialog_digital, container, false);
			break;
		case DO:
			v = inflater.inflate(R.layout.config_dialog_digital, container, false);
			break;
		default:
			break;
		}
		nameEdit = (EditText) v.findViewById(R.id.name);
		scaleEdit = (EditText) v.findViewById(R.id.scale);

		unitEdit = (EditText) v.findViewById(R.id.unit);
		hwAddress = (Spinner) v.findViewById(R.id.hwaddress);
		okButton = (Button) v.findViewById(R.id.okbutton);
		cancelButton = (Button) v.findViewById(R.id.cancelbutton);

		nameEdit.setText(io.name);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (nameEdit.getText().toString().trim().isEmpty()) {
					Toast.makeText(context, "Please provide a name",
							Toast.LENGTH_SHORT).show();
				} else {

					setValues();
					IODialogManager.this.dismiss();
				}

			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IODialogManager.this.dismiss();
			}
		});
		getDialog().setTitle(title);

		switch (io.type) {
		case AI:
			hwAddress.setAdapter(new AddressIdentifierAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, io,
					IO.AnalogIn.values()));
			hwAddress.setSelection(Arrays.asList(IO.AnalogIn.values()).indexOf(
					io.address));
			scaleEdit.setText(((AnalogIn) io).scale);
			unitEdit.setText(((AnalogIn) io).unit);
			break;
		case AO:
			hwAddress.setAdapter(new AddressIdentifierAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, io,
					IO.AnalogOut.values()));
			hwAddress.setSelection(Arrays.asList(IO.AnalogOut.values())
					.indexOf(io.address));
			scaleEdit.setText(((AnalogOut) io).scale);
			unitEdit.setText(((AnalogIn) io).unit);
			break;
		case DI:
			hwAddress.setAdapter(new AddressIdentifierAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, io,
					IO.DigitalIn.values()));
			hwAddress.setSelection(Arrays.asList(IO.DigitalIn.values())
					.indexOf(io.address));
			break;
		case DO:
			hwAddress.setAdapter(new AddressIdentifierAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, io,
					IO.DigitalOut.values()));
			hwAddress.setSelection(Arrays.asList(IO.DigitalOut.values())
					.indexOf(io.address));
			break;
		default:
			break;
		}

		/*
		 * View v = inflater.inflate(R.layout.fragment_dialog, container,
		 * false); View tv = v.findViewById(R.id.textView1); ((TextView)
		 * tv).setText("Dialog Text");
		 * 
		 * // Watch for button clicks. Button button = (Button)
		 * v.findViewById(R.id.button1); button.setOnClickListener(new
		 * OnClickListener() { public void onClick(View v) { // When button is
		 * clicked, call up to owning activity. IODialogManager.this.dismiss();
		 * } });
		 */
		return v;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_input_add);
	}

	protected void setValues() {
		boolean changeIDupdate = false;
		if (!io.name.equals(nameEdit.getText().toString())) {
			io.name = nameEdit.getText().toString();
			changeIDupdate = true;
		}
		if (!io.address.equals((AddressIdentifier) hwAddress.getSelectedItem())) {
			io.address = (AddressIdentifier) hwAddress.getSelectedItem();
			changeIDupdate = true;
		}
		switch (io.type) {
		case AI:
			AnalogIn ai = (AnalogIn) io;
			if (!ai.scale.equals(scaleEdit.getText().toString())) {
				ai.scale = scaleEdit.getText().toString();
				changeIDupdate = true;
			}
			if (!ai.unit.equals(unitEdit.getText().toString())) {
				ai.unit = unitEdit.getText().toString();
				changeIDupdate = true;
			}
			io = ai;
			break;
		case AO:
			AnalogOut ao = (AnalogOut) io;
			if(ao.scale.equals(scaleEdit.getText().toString())){
				ao.scale = scaleEdit.getText().toString();
				changeIDupdate = true;
			}
			if(ao.unit.equals(unitEdit.getText().toString())){
			ao.unit = unitEdit.getText().toString();
				changeIDupdate = true;
			}
			io = ao;
			break;
		case DI:
			// v = inflater.inflate(R.layout.dialog_di, container, false);
			break;
		case DO:
			// v = inflater.inflate(R.layout.dialog_do, container, false);
			break;
		default:
			break;
		}
		if (newIO) {
			changeIDupdate=true;
			((ConfigActivity) getActivity()).config.add(io);
		}
		if (changeIDupdate){
			config.setChangeId();
		}

	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ConfigActivity) getActivity()).adapter.notifyDataSetChanged();

	}

}
