package ch.fhnw.students.keller.benjamin.sarha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ConfigManagerActivity;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.StateMachineManagerActivity;

public class SARHAActivity extends Activity {
	private ImageView ivStatemachine, ivConfig, ivSettings, ivRemote;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ivRemote = (ImageView) findViewById(R.id.ivRemote);
        ivConfig = (ImageView) findViewById(R.id.ivConfig);
        ivStatemachine = (ImageView) findViewById(R.id.ivStatemachine);
        ivSettings = (ImageView) findViewById(R.id.ivSettings);
        
        
        ivStatemachine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),StateMachineManagerActivity.class);
				startActivity(i);
			}
		});
        
        ivConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),ConfigManagerActivity.class);
				startActivity(i);
			}
		});
    }
}