package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;



public class MachineActivity extends Activity {
	
	private StateMachine fsm;
	private String[] values;
	private ArrayAdapter<State> adapter;
	private AlertDialog alertDia;
	private Handler myHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_statemachine);
		fsm= AppData.stateMachine;
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		values = new String[fsm.size()];
		for (int i = 0; i < fsm.size(); i++) {
			
			values[i]=fsm.get(i).getStateName();
		}
		adapter = new ArrayAdapter<State>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, fsm);

		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(view.getContext(),StateActivity.class);
				i.putExtra("StateIndex", position);
				startActivity(i);
				
			}
			
		});
		
		alertDia = new AlertDialog.Builder(this).create();
		alertDia.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});

			}
		});
		alertDia.setCancelable(true);
		// Delete Dialog bei Longclick
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {	

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int pos, long id) {
				final int finalpos = pos;
				alertDia.setTitle("Delete Device \""
						+ fsm.get(pos)
						+ "\"?");
				alertDia.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								fsm.remove(finalpos);
								MachineActivity.this.adapter.notifyDataSetChanged();
							}
						});
				alertDia.show();
				
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		fsm.add(new State(fsm, "NewState"+(fsm.size()+1)));
		adapter.notifyDataSetChanged();
		return true;
	}

	
}
