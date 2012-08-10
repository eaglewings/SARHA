package ch.fhnw.students.keller.benjamin.sarha.config.ui;

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

	public IODialogManager(Context context, IOs item, boolean newIO) {
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
		View v = null;

		switch (io.type) {
		case AI:
			v = inflater.inflate(R.layout.config_dialog_analog, container,
					false);
			scaleEdit = (EditText) v.findViewById(R.id.scale);

			unitEdit = (EditText) v.findViewById(R.id.unit);
			scaleEdit.setText(((AnalogIn) io).scale);
			unitEdit.setText(((AnalogIn) io).unit);
			break;
		case AO:
			v = inflater.inflate(R.layout.config_dialog_analog, container,
					false);
			scaleEdit = (EditText) v.findViewById(R.id.scale);

			unitEdit = (EditText) v.findViewById(R.id.unit);
			scaleEdit.setText(((AnalogOut) io).scale);
			unitEdit.setText(((AnalogOut) io).unit);
			break;
		case DI:
		case DO:
		case TMR:
			v = inflater.inflate(R.layout.config_dialog_digital_timer, container,
					false);
			break;


		default:
			break;
		}
		nameEdit = (EditText) v.findViewById(R.id.name);
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

		hwAddress.setAdapter(new AddressIdentifierAdapter(context,
				android.R.layout.simple_spinner_dropdown_item, io, IO
						.getAddressIdentifiersOfType(io.type)));
		hwAddress.setSelection(io.address.getOrdinal());

		String title = new String();
		if (newIO) {
			title = "New " + IO.typeDescriptors.get(io.type);
		} else {
			title = "Edit " + IO.typeDescriptors.get(io.type);
		}
		getDialog().setTitle(title);
		return v;
	}

	protected void setValues() {
		boolean changeIDupdate = false;
		if (!io.name.equals(nameEdit.getText().toString())) {
			io.name = nameEdit.getText().toString();
			changeIDupdate = true;
		}
		if (!io.address.equals(hwAddress.getSelectedItem())) {
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
			if (ao.scale.equals(scaleEdit.getText().toString())) {
				ao.scale = scaleEdit.getText().toString();
				changeIDupdate = true;
			}
			if (ao.unit.equals(unitEdit.getText().toString())) {
				ao.unit = unitEdit.getText().toString();
				changeIDupdate = true;
			}
			io = ao;
			break;
		case DI:
			break;
		case DO:
			break;
		default:
			break;
		}
		if (newIO) {
			changeIDupdate = true;
			((ConfigActivity) getActivity()).config.add(io);
		}
		if (changeIDupdate) {
			config.setChangeId();
		}

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ConfigActivity) getActivity()).adapter.notifyDataSetChanged();

	}

}
