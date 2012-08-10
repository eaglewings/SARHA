package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.remote.ui.DevicesDialogFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class UploadDownloadConfigActivity extends FragmentActivity {
	private ListView lvConfigs;
	private TextView tvConfigDeviceName, tvConfigDeviceId;
	private ConfigManagerAdapter adapter;
	private String name;
	private int createId, changeId;
	Thread okThread = new Thread(){
		public void run(){
			onResume();
		}
	};
	
private OnClickListener downloadClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(tvConfigDeviceName.getText().toString().equals("No device connected")){
				DevicesDialogFragment.newInstance(null,null).show(
						getSupportFragmentManager(), "dialog");
			}
			else{
				ImportConfigDialogFragment.newInstance(name, createId, changeId).show(getSupportFragmentManager(), "dialog");

			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_activity_upload_download);
		lvConfigs = (ListView) findViewById(R.id.lvConfigs);
		tvConfigDeviceId= (TextView) findViewById(R.id.tvConfigDeviceId);
		tvConfigDeviceName = (TextView) findViewById(R.id.tvConfigDeviceName);
		tvConfigDeviceName.setText("No device connected");
		tvConfigDeviceName.setOnClickListener(downloadClickListener);
		tvConfigDeviceId.setVisibility(View.GONE);
		tvConfigDeviceId.setOnClickListener(downloadClickListener);
		
		adapter = new ConfigManagerAdapter(this, AppData.data.configs);
		lvConfigs.setAdapter(adapter);
		lvConfigs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				UploadConfigDialogFragment.newInstance((Config) lvConfigs.getItemAtPosition(pos)).show(getSupportFragmentManager(), "dialog");

			}
		});


	}
	@Override
	protected void onResume() {
		//System.out.println("resume");
		if (CommManager.connectedDevice == null) {
			DevicesDialogFragment.newInstance(okThread,null).show(
					getSupportFragmentManager(), "dialog");
		}
		super.onResume();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			if (CommManager.connectedDevice != null) {
				String id[] = CommManager.protocol.getConfigId();
				if (id != null) {
					if (id[0] != null && id[1] != null && id[2] != null) {
						name = id[0];
						createId = Integer.parseInt(id[1]);
						changeId = Integer.parseInt(id[2]);
						if(name.equals("0")){
							tvConfigDeviceName.setText("No config on device \""+CommManager.connectedDevice.getName()+"\"");
						}
						else{
							tvConfigDeviceName.setText(name);
						tvConfigDeviceId.setText(
								Utils.idString(createId, changeId));
						tvConfigDeviceId.setVisibility(View.VISIBLE);
						}
						
						
					}
				}
			}
		}
		super.onWindowFocusChanged(hasFocus);
	}
}
