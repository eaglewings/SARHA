package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ConfigActivity;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.remote.ui.DevicesDialogFragment;

public class StateMachineActivity extends FragmentActivity {

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
		fsm = AppData.currentWorkingStateMachine;

		ListView listView = (ListView) findViewById(R.id.listView1);
		values = new String[fsm.size()];
		for (int i = 0; i < fsm.size(); i++) {

			values[i] = fsm.get(i).getStateName();
		}
		adapter = new ArrayAdapter<State>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, fsm);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(view.getContext(), StateActivity.class);
				AppData.currentWorkingState = adapter.getItem(position);
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
				alertDia.setTitle("Delete State \"" + fsm.get(pos) + "\"?");
				alertDia.setButton(DialogInterface.BUTTON_POSITIVE, "Delete",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								fsm.remove(finalpos);
								StateMachineActivity.this.adapter
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
		inflater.inflate(R.menu.fsm_statemachine, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addstate: {
			fsm.add(new State(fsm, "NewState" + (fsm.size() + 1)));
			adapter.notifyDataSetChanged();
			break;
		}
		case R.id.parseStateMachine: {
			OutputStream fos = null;
			File file = new File(Environment.getExternalStorageDirectory()
					.toString(), "program.lua");
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
			UploadStateMachineDialogFragment.newInstance(fsm).show(
					getSupportFragmentManager(), "dialog");
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
