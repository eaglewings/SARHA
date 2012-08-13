package ch.fhnw.students.keller.benjamin.sarha.settings.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.comm.Device;

public class DeviceSettingsDialogFragment extends DialogFragment {
	private EditText etName;
	private Button btOk, btCancel;
	private CheckBox cbDelete;
	private TextView tvInfo;
	private Device device;

	public static DeviceSettingsDialogFragment newInstance(Device device) {
		DeviceSettingsDialogFragment f = new DeviceSettingsDialogFragment();
		f.device = device;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.settings_dialog_device, container);
		etName = (EditText) v.findViewById(R.id.etName);
		etName.setText(device.getName());
		tvInfo =(TextView) v.findViewById(R.id.tvInfo);
		cbDelete = (CheckBox) v.findViewById(R.id.cbDelete);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		if (!device.equals(CommManager.connectedDevice)) {
			etName.setEnabled(false);
			tvInfo.setVisibility(View.VISIBLE);
		}
		
		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cbDelete.isChecked()) {
					if (device.equals(CommManager.connectedDevice)) {
						CommManager.disconnect();
					}
					AppData.data.devices.remove(device);
				} else {
					String name = etName.getText().toString().trim();
					if (name.length() > 26) {
						name = name.substring(0, 26).trim();
					}
					if (device.equals(CommManager.connectedDevice)) {
						if (CommManager.protocol.setDeviceName(name)) {
							device.setName(name);
						}
					}
				}
				dismiss();

			}
		});
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});

		etName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (etName.isEnabled()) {
						etName.selectAll();
					}
				}

			}
		});
		getDialog().setTitle("Device settings for \"" + device.getName() + "\"");
		return v;
	}
}
