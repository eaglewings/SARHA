package ch.fhnw.students.keller.benjamin.sarha.settings.ui;

import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.StateMachineActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

public class SettingsActivity extends FragmentActivity {
	private TabHost tabHost;
	private ListView lvDevices;
	private Button btDeleteAppData, btDisconnect;
	private final String DEVICES_TAB = "Devices Tab";
	private final String PREFERENCES_TAB = "Devices Tab";
	private DeviceAdapter deviceAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		deviceAdapter= new DeviceAdapter(this);
		lvDevices = (ListView) findViewById(R.id.lvDevices);
		lvDevices.setAdapter(deviceAdapter);
		btDeleteAppData = (Button) findViewById(R.id.btDeleteAppData);
		btDisconnect = (Button) findViewById(R.id.btDisconnect);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(DEVICES_TAB).setIndicator("Devices")
				.setContent(R.id.tabDevices));
		tabHost.addTab(tabHost.newTabSpec(PREFERENCES_TAB).setIndicator("Preferences")
				.setContent(R.id.tabPreferences));
		
		CommManager.findDevices();
		lvDevices.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int finalpos = position;
				AlertDialog deleteDialog = new AlertDialog.Builder(SettingsActivity.this).create();
				deleteDialog.setTitle("Delete Device \"" + lvDevices.getItemAtPosition(position) + "\"?");
				deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								AppData.data.devices.remove(lvDevices.getItemAtPosition(finalpos));
								deviceAdapter.notifyDataSetChanged();
								System.out.println(lvDevices.getItemAtPosition(finalpos));
								System.out.println("devices size "+AppData.data.devices.size());
							}
						});
				deleteDialog.show();
				return false;
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
