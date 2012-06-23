package ch.fhnw.students.keller.benjamin.sarha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ConfigManagerActivity;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.StateMachineManagerActivity;
import ch.fhnw.students.keller.benjamin.sarha.remote.ui.RemoteActivity;
import ch.fhnw.students.keller.benjamin.sarha.settings.ui.SettingsActivity;

public class SARHAActivity extends Activity {
	private ImageView ivStatemachine, ivConfig, ivSettings, ivRemote;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppData.handler = new Handler();
		AppData.context = getApplicationContext();
		AppData.createInstance(getApplicationContext());
		
		setContentView(R.layout.main);

		ivRemote = (ImageView) findViewById(R.id.ivRemote);
		ivConfig = (ImageView) findViewById(R.id.ivConfig);
		ivStatemachine = (ImageView) findViewById(R.id.ivStatemachine);
		ivSettings = (ImageView) findViewById(R.id.ivSettings);

		ivRemote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), RemoteActivity.class);
				startActivity(i);
			}
		});

		ivStatemachine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						StateMachineManagerActivity.class);
				startActivity(i);
			}
		});

		ivConfig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						ConfigManagerActivity.class);
				startActivity(i);
			}
		});
		ivSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						SettingsActivity.class);
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
}