package ch.fhnw.students.keller.benjamin.sarha.settings.ui;

import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import android.widget.TabHost;

public class SettingsActivity extends FragmentActivity {
	private TabHost tabHost;
	private final String DEVICES_TAB = "Devices Tab";
	private final String PREFERENCES_TAB = "Devices Tab";
	private DeviceAdapter deviceAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		deviceAdapter= new DeviceAdapter(this);
		ListView listViewDevices = (ListView) findViewById(R.id.lvDevices);
		listViewDevices.setAdapter(deviceAdapter);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(DEVICES_TAB).setIndicator("Devices")
				.setContent(R.id.tabDevices));
		tabHost.addTab(tabHost.newTabSpec(PREFERENCES_TAB).setIndicator("Preferences")
				.setContent(R.id.tabPreferences));
		
		CommManager.findDevices();

		
		
		
		
		
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
}
