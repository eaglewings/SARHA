package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;



public class StateMachineManagerActivity extends Activity {
	private StateMachineManagerAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_statemachinemanager);
		
		ListView list = (ListView) findViewById(R.id.listView1);
		adapter= new StateMachineManagerAdapter(this, AppData.stateMachines);
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
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fsm_statemachinemanager, menu);
		return true;
	}
}
