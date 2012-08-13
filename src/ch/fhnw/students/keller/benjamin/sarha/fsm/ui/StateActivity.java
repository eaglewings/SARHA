package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

public class StateActivity extends FragmentActivity {
	private StateAdapter stateAdapter;
	private State state;
	private StateMachine stateMachine;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_state);
		state = AppData.currentWorkingState;
		stateMachine = AppData.currentWorkingStateMachine;

		ListView listViewTransitions = (ListView) findViewById(R.id.listViewTransitions);
		CheckBox cbInitial = (CheckBox) findViewById(R.id.cbInitial);
		if (state.equals(stateMachine.getInitialState())) {
			cbInitial.setChecked(true);
		}
		cbInitial.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					stateMachine.setInitialState(state);
					stateMachine.setChangeId();
				}
			}
		});
		TextView txtView = (TextView) findViewById(R.id.textView2);
		txtView.setText(state.getStateName());

		stateAdapter = new StateAdapter(this, state);

		listViewTransitions.setAdapter(stateAdapter);

		listViewTransitions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(view.getContext(), TransitionActivity.class);
				AppData.currentWorkingTransition = stateAdapter.getItem(position);
				startActivity(i);

			}

		});

		listViewTransitions.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
				TransitionDialogFragment.newInstance(stateAdapter.getItem(pos)).show(getSupportFragmentManager(), "dialog");
				return true;
			}
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			stateAdapter.notifyDataSetChanged();
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fsm_state, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addTransition:
			TransitionDialogFragment.newInstance(null).show(getSupportFragmentManager(), "dialog");
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		stateAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
}
