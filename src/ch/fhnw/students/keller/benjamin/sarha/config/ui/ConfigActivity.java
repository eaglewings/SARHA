package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.AnalogIn;
import ch.fhnw.students.keller.benjamin.sarha.config.AnalogOut;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.DigitalIn;
import ch.fhnw.students.keller.benjamin.sarha.config.DigitalOut;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class ConfigActivity extends FragmentActivity {
	public ConfigAdapter adapter;
	public Config config;
	
	/** Called when the activity is first created. */
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int position =getIntent().getExtras().getInt("ConfigIndex");
		config=AppData.data.configs.get(position);
		adapter = new ConfigAdapter(this, config);
		
		setContentView(R.layout.config_activity_config);
		
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
	}
	
	void showDialog(Object item, boolean newIO) {
		System.out.println("item: "+ item+ " type: "+((IOs)item).type);
	    DialogFragment newFragment = new IODialogManager(this,(IOs) item, newIO);
	    newFragment.show(getSupportFragmentManager(), "dialog");
	    

	    
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.config_config, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.anin) {
			showDialog(new AnalogIn(),true);
			return true;
		} else if (item.getItemId() == R.id.anout) {
			showDialog(new AnalogOut(),true);
			return true;
		} else if (item.getItemId() == R.id.digin) {
			showDialog(new DigitalIn(),true);
			return true;
		} else if (item.getItemId() == R.id.digout) {
			showDialog(new DigitalOut(),true);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
	
}
