package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import ch.fhnw.students.keller.benjamin.sarha.fsm.AnalogInCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.AnalogOutAction;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition.ConditionTypes;
import ch.fhnw.students.keller.benjamin.sarha.fsm.DigitalInCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.DigitalOutAction;
import ch.fhnw.students.keller.benjamin.sarha.fsm.OperationCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.TimerAction;
import ch.fhnw.students.keller.benjamin.sarha.fsm.TimerCondition;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;
import ch.fhnw.students.keller.benjamin.tree.TreeView;

public class TransitionActivity extends FragmentActivity {

	private StateMachine fsm;
	private ArrayAdapter<State> adapter;
	private Transition transition;
	private State state;
	public ActionAdapter actionAdapter;
	private State[] values;
	private TabHost mTabHost;
	
	private final String ACTION_TAB = "Action Tab";
	private final String CONDITION_TAB = "Condition Tab";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_transition);

		fsm = AppData.currentWorkingStateMachine;
		state=AppData.currentWorkingState;
		transition= AppData.currentWorkingTransition;
		ListView listViewActions = (ListView) findViewById(R.id.listViewActions);
		TextView txtViewFromState = (TextView) findViewById(R.id.textViewFromState);
		TextView txtViewTransition = (TextView) findViewById(R.id.textViewTransition);
		Spinner spinner = (Spinner) findViewById(R.id.spinnerToState);
		TreeView conditionView = (TreeView) findViewById(R.id.conditionView);
		txtViewFromState.setText(state.getStateName());
		txtViewTransition.setText(transition.toString());
		conditionView.setAdapter(new ConditionAdapter(transition.condition, this));
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec(ACTION_TAB).setIndicator("Actions")
				.setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec(CONDITION_TAB)
				.setIndicator("Conditions").setContent(R.id.tab2));
		
		
		int selected = 0;
		values = new State[fsm.size() - 1];
		int j = 0;
		for (int i = 0; i < fsm.size(); i++) {
			if (fsm.get(i) != state) {

				values[j] = fsm.get(i);
				if(fsm.get(i).equals(transition.getToState())){
					selected=j;
				}
				j++;
			}

		}
		actionAdapter = new ActionAdapter(this, transition.actions);

		listViewActions.setAdapter(actionAdapter);

		adapter = new ArrayAdapter<State>(this,
				android.R.layout.simple_spinner_item,
				android.R.id.text1, values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(selected);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				transition.setToState(values[pos]);
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		if (mTabHost.getCurrentTabTag().equals(ACTION_TAB)) {
			inflater.inflate(R.menu.fsm_transition_action, menu);
			
		} else if (mTabHost.getCurrentTabTag().equals(CONDITION_TAB)) {
			inflater.inflate(R.menu.fsm_transition_condition, menu);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addAOaction:
			showActionDialog(new AnalogOutAction());
			break;
		case R.id.addDOaction:
			showActionDialog(new DigitalOutAction());
			break;
		case R.id.addTMRaction:
			showActionDialog(new TimerAction());
			break;
		case R.id.ai_condition:
			showConditionDialog(ConditionTypes.AI);
			break;
		case R.id.di_condition:
			showConditionDialog(ConditionTypes.DI);
			break;
		case R.id.tmr_condition:
			showConditionDialog(ConditionTypes.TMR);
			break;
		case R.id.operation_condition:
			showConditionDialog(ConditionTypes.OPERATION);
			break;
		case R.id.delete_condition:
			transition.condition.deleteNode();
			break;
		default:
			break;
		}

		return true;
	}

	private void showConditionDialog(Condition.ConditionTypes type) {
		Condition condition;
		Condition parent = (Condition) transition.condition.getSelectedNode();
		if (parent == null) {
			parent = (Condition) transition.condition.getRoot();
		}
		switch (type) {
		case OPERATION:
			condition = new OperationCondition(parent);
			break;
		case DI:
			condition = new DigitalInCondition(parent);
			break;
		case AI:
			condition = new AnalogInCondition(parent);
			break;
		case TMR:
			condition = new TimerCondition(parent);
			break;
		default:
			return;
		}		
		if (parent.canHaveMoreChildren()) {
			DialogFragment newFragment = new ConditionDialog(this, condition,
					true);
			newFragment.show(getSupportFragmentManager(), "dialog");
		}

	}

	private void showActionDialog(Action action) {
		DialogFragment newFragment = new ActionDialog(this, action, true);
		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	@Override
	public void onResume() {
		super.onResume();
		actionAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
}
