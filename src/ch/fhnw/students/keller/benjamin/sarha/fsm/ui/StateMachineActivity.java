package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.UploadDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;

public class StateMachineActivity extends FragmentActivity {

	private StateMachine fsm;
	private ArrayAdapter<State> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_statemachine);
		fsm = AppData.currentWorkingStateMachine;

		ListView listView = (ListView) findViewById(R.id.listView1);
		
		adapter = new ArrayAdapter<State>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fsm);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(view.getContext(), StateActivity.class);
				AppData.currentWorkingState = adapter.getItem(position);
				startActivity(i);

			}

		});

		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
				StateDialogFragment.newInstance((State) adapter.getItemAtPosition(pos)).show(getSupportFragmentManager(), "dialog");

				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fsm_statemachine, menu);
		return true;
	}
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			adapter.notifyDataSetChanged();
		}
		super.onWindowFocusChanged(hasFocus);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addstate: {
			StateDialogFragment.newInstance(null).show(getSupportFragmentManager(), "dialog");
			break;
		}
		case R.id.parseStateMachine: {
			OutputStream fos = null;
			File file = new File(Environment.getExternalStorageDirectory().toString(), "program.lua");
			try {
				fos = new FileOutputStream(file);
				fos.write(fsm.parse().getBytes());
				fos.flush();
				fos.close();

				Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			}
			break;
		}
		case R.id.uploadStateMachine: {
			UploadDialogFragment.newInstance(fsm).show(getSupportFragmentManager(), "dialog");
		}
		}

		return true;
	}

	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
}
