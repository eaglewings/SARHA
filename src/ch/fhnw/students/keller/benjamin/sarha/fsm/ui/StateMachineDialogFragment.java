package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ConfigSpinnerAdapter;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;

public class StateMachineDialogFragment extends DialogFragment {
	private StateMachine stateMachine;
	private EditText etName;
	private Button btOk, btCancel;
	private CheckBox cbDelete;
	private Spinner spConfig;
	private ConfigSpinnerAdapter adapter;

	static StateMachineDialogFragment newInstance(StateMachine stateMachine) {
		StateMachineDialogFragment f = new StateMachineDialogFragment();

		f.stateMachine = stateMachine;

		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fsm_dialog_statemachine, container);
		getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
		etName = (EditText) v.findViewById(R.id.etName);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		cbDelete = (CheckBox) v.findViewById(R.id.cbDelete);
		spConfig = (Spinner) v.findViewById(R.id.spConfig);
		System.out.println("newadapter");
		adapter = new ConfigSpinnerAdapter(this.getActivity());
		spConfig.setAdapter(adapter);

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});

		if (stateMachine == null) {
			getDialog().setTitle("New Statemachine");
			etName.setText("new statemachine");
			getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_menu_add);
			cbDelete.setVisibility(View.GONE);
			btOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String name = etName.getText().toString().trim();
					if (name.equals("")) {
						Toast.makeText(StateMachineDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
					} else {
						AppData.data.stateMachines.add(new StateMachine(name, (Config) spConfig.getSelectedItem()));
						StateMachineDialogFragment.this.dismiss();
					}
					
				}
			});

		} else {
			getDialog().setTitle("Edit Statemachine");
			getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_info);
			etName.setText(stateMachine.getName());
			cbDelete.setVisibility(View.VISIBLE);

			spConfig.setSelection(((ArrayAdapter<Config>) spConfig.getAdapter()).getPosition(stateMachine.getConfig()));
			btOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String name = etName.getText().toString().trim();
					if (name.equals("")) {
						Toast.makeText(StateMachineDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
					} else {
						if (!stateMachine.getName().equals(name) || stateMachine.getConfig() != spConfig.getSelectedItem()) {
							stateMachine.setChangeId();
						}
						stateMachine.setName(name);
						stateMachine.setConfig((Config) spConfig.getSelectedItem());
						StateMachineDialogFragment.this.dismiss();
					}
					if (cbDelete.isChecked()) {
						AppData.data.stateMachines.remove(stateMachine);
						StateMachineDialogFragment.this.dismiss();
					}

				}
			});

		}

		return v;
	}
}
