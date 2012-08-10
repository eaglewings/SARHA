package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.ConfigImporter;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceModel;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.DownloadUploadConfigDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.UploadDownloadConfigActivity;

public class RemoteActivity extends FragmentActivity implements Observer {
	private Config cfg;
	private Button btProg, btDebug;
	private DeviceModel device;
	private LinearLayout lla;
	private ViewStock viewStock;
	private boolean offline=false;
	Thread cancelThread = new Thread(){
		public void run() {
			offline= true;
		};
	};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_activity);
		lla = (LinearLayout) findViewById(R.id.linearLayout3);
		btProg = (Button) findViewById(R.id.button1);
		btProg.setText("Prog run");
		btDebug = (Button) findViewById(R.id.button2);
		btDebug.setText("Debug");
		cfg= IO.defaultConfig();
		device= new DeviceModel(cfg);
		device.addObserver(this);
		if (device.isProgramRunning()) {
			btProg.setText("Prog stop");
		} else {
			btProg.setText("Prog run");
		}
		btProg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (btProg.getText().equals("Prog stop")) {
					device.programStop();
				} else {
					device.programRun();
				}

			}
		});
		if (device.isProgramRunning()) {
			btDebug.setEnabled(true);
		} else {
			btDebug.setEnabled(false);
		}
		btDebug.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (device.isDebug()) {
					device.debugInactive();
				} else {
					device.debugActive();
				}

			}
		});
		
		
		
	}
	
	private boolean checkConfig(){
		Config config;
		if (CommManager.protocol != null) {
			String[] id = CommManager.protocol.getConfigId();
			System.out.println("setconfig" + id[0]+" "+id[1]+" "+id[2]);
			if (id != null) {
				if (id[0] != null && id[1] != null && id[2] != null) {
					String name = id[0];
					int createId = Integer.parseInt(id[1]);
					int changeId = Integer.parseInt(id[2]);
					if(ConfigImporter.getMatchType(name, createId,
							changeId)== ConfigImporter.CompareResult.MATCH) {

						config = ConfigImporter.getMatchedConfig(name,
								createId, changeId);
						
						System.out.println("match");
						if(config.equals(cfg)){
							return true;
						}
						else{
							cfg=config;
							device = new DeviceModel(cfg);
							device.addObserver(this);
							viewStock = new ViewStock(this, device,
									CommManager.protocol);
							viewStock.addViews(lla);
							AppData.currentWorkingDeviceModel = device;
							return true;
						}
					
					}
				}
			}
		}
		return false;
	}
	@Override
	public void update(Observable observable, Object data) {
		if (device.isProgramRunning()) {
			btProg.setText("Prog stop");
			btDebug.setEnabled(true);
		} else {
			btProg.setText("Prog run");
			btDebug.setEnabled(false);
		}

	}

	

	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		System.out.println("focus changed"+ Boolean.toString(hasFocus));
		if(hasFocus){
			if (CommManager.connectedDevice == null) {
				if(offline){
				
				device = new DeviceModel(cfg);
				viewStock = new ViewStock(this, device, null);
				device.addObserver(this);
				viewStock.addViews(lla);
				AppData.currentWorkingDeviceModel = device;
				}
				else{
					DevicesDialogFragment.newInstance(null,cancelThread).show(
							getSupportFragmentManager(), "dialog");
				}
			}
			else{
				if(checkConfig()){
					CommManager.protocol.setRemote(true);
				}
				else{
					Intent i = new Intent(this, UploadDownloadConfigActivity.class);
					startActivity(i);
				}
			}
			if(AppData.currentWorkingDeviceModel!= null){
				cfg=AppData.currentWorkingDeviceModel.getConfig();
			}
			else{
				cfg= IO.defaultConfig();
			}
		}
		else{
			if(CommManager.connectedDevice!=null){
				CommManager.protocol.setRemote(false);//deactivate periodic ControlUnit IO Status messages
			}
			AppData.saveAppData();
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	public void setConfig() {
		if (CommManager.protocol != null) {
			String[] id = CommManager.protocol.getConfigId();
			System.out.println("setconfig" + id[0]+" "+id[1]+" "+id[2]);
			if (id != null) {
				if (id[0] != null && id[1] != null && id[2] != null) {
					String name = id[0];
					int createId = Integer.parseInt(id[1]);
					int changeId = Integer.parseInt(id[2]);
					switch (ConfigImporter.getMatchType(name, createId,
							changeId)) {

					case MATCH:
						cfg = ConfigImporter.getMatchedConfig(name,
								createId, changeId);
						System.out.println("match");
						break;
					default:
						Intent i = new Intent(this, UploadDownloadConfigActivity.class);
						startActivity(i);
					}
					System.out.println(Utils.idString(createId, changeId));
					device = new DeviceModel(cfg);
					device.addObserver(this);
					viewStock = new ViewStock(this, device,
							CommManager.protocol);
					viewStock.addViews(lla);
					AppData.currentWorkingDeviceModel = device;
				}
			}
			
		} else {
			device = new DeviceModel(cfg);
			viewStock = new ViewStock(this, device, null);
			device.addObserver(this);
			viewStock.addViews(lla);
			AppData.currentWorkingDeviceModel = device;
		}

		
	}
	
}