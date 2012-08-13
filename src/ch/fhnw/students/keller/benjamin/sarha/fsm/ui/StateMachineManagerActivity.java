package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
import android.widget.ListView;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.ExportDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.ImportDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.UploadDownloadActivity;
import ch.fhnw.students.keller.benjamin.sarha.Importer.PortableType;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;



public class StateMachineManagerActivity extends FragmentActivity {
	private StateMachineManagerAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_statemachinemanager);
		
		ListView list = (ListView) findViewById(R.id.listView1);
		adapter= new StateMachineManagerAdapter(this, AppData.data.stateMachines);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(view.getContext(),StateMachineActivity.class);
				AppData.currentWorkingStateMachine=(StateMachine) adapter.getItem(position);
				startActivity(i);
				
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				StateMachineDialogFragment.newInstance((StateMachine) adapter.getItem(position)).show(getSupportFragmentManager(), "dialog");
				
				return false;
			}
		});
	}
	@Override
	protected void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			adapter.notifyDataSetChanged();
		}
		super.onWindowFocusChanged(hasFocus);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fsm_statemachinemanager, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menuitemnewstatemachine:
			StateMachineDialogFragment.newInstance(null).show(getSupportFragmentManager(), "dialog");
			break;
		case R.id.menuitemimportstatemachine:
			ImportDialogFragment.newInstance(PortableType.STATEMACHINE, null,0,0).show(getSupportFragmentManager(), "dialog");
			break;
		case R.id.menuitemexportstatemachine:
			ExportDialogFragment.newInstance(PortableType.STATEMACHINE).show(getSupportFragmentManager(), "dialog");
			break;
		case  R.id.menuitemdownloadconfig:
			AppData.uploadDownloadPortableType=PortableType.STATEMACHINE;
			Intent intent = new Intent(this,UploadDownloadActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
	
}
