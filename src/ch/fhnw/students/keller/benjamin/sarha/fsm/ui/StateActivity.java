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

public class StateActivity extends Activity{
	private StateAdapter stateAdapter;
	private State state;
	private StateMachine stateMachine;
	private AlertDialog alertDia;
	private Handler myHandler = new Handler();
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_state);
		state = AppData.currentWorkingState;
		stateMachine = AppData.currentWorkingStateMachine;

		ListView listViewTransitions = (ListView) findViewById(R.id.listViewTransitions);
		CheckBox cbInitial = (CheckBox) findViewById(R.id.cbInitial);
		if(state.equals(stateMachine.getInitialState())){
			cbInitial.setChecked(true);
		}
		cbInitial.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
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
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(view.getContext(),
						TransitionActivity.class);
				AppData.currentWorkingTransition=stateAdapter.getItem(position);
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
						stateAdapter.notifyDataSetChanged();
					}
				});

			}
		});
		alertDia.setCancelable(true);
		// Delete Dialog on longclick
		listViewTransitions
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View view, int pos, long id) {
						final int finalpos = pos;
						alertDia.setTitle("Delete State \"" + state.get(pos)
								+ "\"?");
						alertDia.setButton(DialogInterface.BUTTON_POSITIVE,
								"Delete",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										state.remove(finalpos);
										stateMachine.setChangeId();
										StateActivity.this.stateAdapter
												.notifyDataSetChanged();
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
		inflater.inflate(R.menu.fsm_state, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.addTransition) {
			state.add(new Transition(state, "NewTransition"
					+ (state.size() + 1)));
			stateAdapter.notifyDataSetChanged();
			return true;
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
