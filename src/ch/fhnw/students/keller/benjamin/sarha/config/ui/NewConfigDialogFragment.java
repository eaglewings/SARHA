package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.Importer;

public class NewConfigDialogFragment extends DialogFragment {
	private EditText etName;
	private Button btOk, btCancel;
	private Config config;
	private CheckBox cbDelete;

	static NewConfigDialogFragment newInstance(Config config) {
		NewConfigDialogFragment f = new NewConfigDialogFragment();
		f.config = config;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.config_dialog_config, container);
		etName = (EditText) v.findViewById(R.id.etName);
		btOk = (Button) v.findViewById(R.id.btOk);
		cbDelete = (CheckBox) v.findViewById(R.id.cbDelete);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});

		if (config == null) {// Create new Config
			getDialog().setTitle("New Config");
			etName.setText("new config");
			cbDelete.setVisibility(View.GONE);
			btOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String name = etName.getText().toString().trim();
					if (!Utils.checkPortableName(Importer.PortableType.CONFIG, name)) {
						Toast.makeText(NewConfigDialogFragment.this.getActivity(), "Name already exists or is invalid", Toast.LENGTH_SHORT).show();
					} else {
						AppData.data.configs.add(new Config(name));
						NewConfigDialogFragment.this.dismiss();
					}
					

				}
			});

		} else { // Edit a Config
			getDialog().setTitle("Edit Config");
			etName.setText(config.name);
			cbDelete.setVisibility(View.VISIBLE);
			btOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String name = etName.getText().toString().trim();
					if (name.equals("")) {
						Toast.makeText(NewConfigDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
					} else {
						if (!config.name.equals(name)) {
							config.setChangeId();
						}
						config.name = name;
						NewConfigDialogFragment.this.dismiss();
					}
					if (cbDelete.isChecked()) {
						AppData.data.configs.remove(config);
						NewConfigDialogFragment.this.dismiss();
					}

				}
			});
		}

		return v;

	}
}
