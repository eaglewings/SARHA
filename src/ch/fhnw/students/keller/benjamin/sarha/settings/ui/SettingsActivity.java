package ch.fhnw.students.keller.benjamin.sarha.settings.ui;

import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.comm.Device;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceState;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.StateMachineActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;

public class SettingsActivity extends FragmentActivity {
	private TabHost tabHost;
	private ListView lvDevices;
	private Button btDeleteAppData, btDisconnect, btRefresh;
	private ProgressBar pbProgress;
	private final String DEVICES_TAB = "Devices Tab";
	private final String PREFERENCES_TAB = "Devices Tab";
	private DeviceAdapter deviceAdapter;
	private class RefreshThread extends Thread{
		public void run() {
			while (CommManager.deviceFinder.isAlive() || pbProgress.getVisibility() == View.VISIBLE) {
				if ((pbProgress.getVisibility() == View.VISIBLE && !CommManager.deviceFinder.isAlive()) || (pbProgress.getVisibility() == View.GONE && CommManager.deviceFinder.isAlive()))
					AppData.handler.post(new Runnable() {
						@Override
						public void run() {
							if (CommManager.deviceFinder.isAlive()) {
								pbProgress.setVisibility(View.VISIBLE);
								btRefresh.setEnabled(false);

							} else {
								pbProgress.setVisibility(View.GONE);
								btRefresh.setEnabled(true);
							}

						}
					});
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		deviceAdapter = new DeviceAdapter(this);
		lvDevices = (ListView) findViewById(R.id.lvDevices);
		lvDevices.setAdapter(deviceAdapter);
		btDeleteAppData = (Button) findViewById(R.id.btDeleteAppData);
		btDisconnect = (Button) findViewById(R.id.btDisconnect);
		btRefresh = (Button) findViewById(R.id.btRefresh);
		pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
		pbProgress.setVisibility(View.GONE);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(DEVICES_TAB).setIndicator("Devices").setContent(R.id.tabDevices));
		tabHost.addTab(tabHost.newTabSpec(PREFERENCES_TAB).setIndicator("Preferences").setContent(R.id.tabPreferences));

		CommManager.findDevices();
		new RefreshThread().start();
		lvDevices.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				DeviceSettingsDialogFragment.newInstance((Device) deviceAdapter.getItem(position)).show(getSupportFragmentManager(), "dialog");
				return true;
			}
		});
		lvDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Device device = (Device) deviceAdapter.getItem(pos);
				if (device.getState() == DeviceState.CONNECTABLE) {
					CommManager.connect(device);
				}
			}
		});

		btDeleteAppData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppData.resetAppData(SettingsActivity.this);
				AppData.saveAppData();

			}
		});
		btDisconnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommManager.disconnect();

			}
		});
		btRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommManager.findDevices();
				new RefreshThread().start();

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		deviceAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			deviceAdapter.notifyDataSetChanged();
		}
		super.onWindowFocusChanged(hasFocus);
	}
}
