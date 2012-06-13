package ch.fhnw.students.keller.benjamin.sarha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ConfigManagerActivity;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.StateMachineManagerActivity;

public class SARHAActivity extends Activity {
	private Button btStateMachine, btConfig;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btStateMachine = (Button) findViewById(R.id.button1);
        btConfig = (Button) findViewById(R.id.button2);
        
        
        btStateMachine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),StateMachineManagerActivity.class);
				startActivity(i);
			}
		});
        
        btConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),ConfigManagerActivity.class);
				startActivity(i);
			}
		});
    }
}