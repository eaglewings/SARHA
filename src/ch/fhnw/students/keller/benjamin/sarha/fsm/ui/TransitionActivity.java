package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Action;
import ch.fhnw.students.keller.benjamin.sarha.fsm.DigitalOutAction;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

public class TransitionActivity extends FragmentActivity  {

	private StateMachine fsm;
	private ArrayAdapter<State> adapter;
	private Transition transition;
	private State state;
	private ActionAdapter actionAdapter;
	private State[] values;
	private TabHost mTabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_transition);

		fsm = AppData.stateMachine;

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		int position = extras.getInt("StateIndex");
		state = fsm.get(position);
		position = extras.getInt("TransitionIndex");
		transition = state.get(position);

		ListView listViewActions = (ListView) findViewById(R.id.listViewActions);
		TextView txtViewFromState = (TextView) findViewById(R.id.textViewFromState);
		TextView txtViewTransition = (TextView) findViewById(R.id.textViewTransition);
		Spinner spinner = (Spinner) findViewById(R.id.spinnerToState);
		txtViewFromState.setText(state.getStateName());
		txtViewTransition.setText(transition.toString());
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
				.setIndicator("Actions").setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
				.setIndicator("Conditions").setContent(R.id.tab2));
		
		values= new State[fsm.size()-1];
		int j=0;
		for (int i = 0; i < fsm.size(); i++) {
			Log.d("transact",""+i);
			if(fsm.get(i)!=state){
				
				values[j]=fsm.get(i);
				j++;
			}
				
		}
		actionAdapter = new ActionAdapter(this, transition.actions);

		listViewActions.setAdapter(actionAdapter);

		adapter=new ArrayAdapter<State>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, values);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				transition.setToState(values[pos]);	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fsm_transition, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.addDOaction) {
			Action action = new DigitalOutAction();
			transition.actions.add(action);
			actionAdapter.notifyDataSetChanged();
			return true;
		}
		if (item.getItemId() == R.id.addCondition) {
			return true;
		}
		return true;
	}
	public void onResume() {
		super.onResume();
		actionAdapter.notifyDataSetChanged();	
	}

}
