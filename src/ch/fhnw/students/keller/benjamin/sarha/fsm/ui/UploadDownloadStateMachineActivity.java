package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.remote.ui.DevicesDialogFragment;

public class UploadDownloadStateMachineActivity extends FragmentActivity {
	private ListView lvStateMachines;
	private TextView tvStateMachineDeviceName, tvStateMachineDeviceId;
	private StateMachineManagerAdapter adapter;
	private String name;
	private int createId, changeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_upload_download);
		lvStateMachines = (ListView) findViewById(R.id.lvStateMachines);
		tvStateMachineDeviceId= (TextView) findViewById(R.id.tvStateMachineDeviceId);
		tvStateMachineDeviceName = (TextView) findViewById(R.id.tvStateMachineDeviceName);
		tvStateMachineDeviceName.setText("No device connected");
		tvStateMachineDeviceName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tvStateMachineDeviceName.getText().toString().equals("No device connected")){
					DevicesDialogFragment.newInstance(null,null).show(
							getSupportFragmentManager(), "dialog");
				}
			}
		});
		tvStateMachineDeviceId.setVisibility(View.GONE);
		if (CommManager.connectedDevice != null) {

			String id[] = CommManager.protocol.getStateMachineId();
			if (id != null) {
				if (id[0] != null && id[1] != null && id[2] != null) {
					name = id[0];
					createId = Integer.parseInt(id[1]);
					changeId = Integer.parseInt(id[2]);
					if(name.equals("0")){
						tvStateMachineDeviceName.setText("No statemachine on device \""+CommManager.connectedDevice.getName()+"\"");
					}
					else{
						tvStateMachineDeviceName.setText(name);
					tvStateMachineDeviceId.setText(
							Utils.idString(createId, changeId));
					tvStateMachineDeviceId.setVisibility(View.VISIBLE);
					}
					
					
				}
			}
		}
		else{
			DevicesDialogFragment.newInstance(null,null).show(
					getSupportFragmentManager(), "dialog");
		}
		
		adapter = new StateMachineManagerAdapter(this, AppData.data.stateMachines);

		lvStateMachines.setAdapter(adapter);

		lvStateMachines.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				UploadStateMachineDialogFragment.newInstance((StateMachine) lvStateMachines.getItemAtPosition(pos)).show(getSupportFragmentManager(), "dialog");

			}
		});


		
		

	}
}

