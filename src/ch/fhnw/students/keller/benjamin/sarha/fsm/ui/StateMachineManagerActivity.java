package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ExportConfigDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;



public class StateMachineManagerActivity extends FragmentActivity {
	private StateMachineManagerAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fsm_activity_statemachinemanager);
		
		ListView list = (ListView) findViewById(R.id.listView1);
		adapter= new StateMachineManagerAdapter(this, AppData.data.stateMachines);
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
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewStateMachineDialogFragment.newInstance((StateMachine) adapter.getItem(position)).show(getSupportFragmentManager(), "dialog");
				
				return false;
			}
		});
	}
	@Override
	protected void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			adapter.notifyDataSetChanged();
		}
		super.onWindowFocusChanged(hasFocus);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fsm_statemachinemanager, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menuitemnewstatemachine:
			NewStateMachineDialogFragment.newInstance(null).show(getSupportFragmentManager(), "dialog");
			break;
		case R.id.menuitemcopystatemachine:
			break;
		case R.id.menuitemimportstatemachine:
			break;
		case R.id.menuitemexportstatemachine:
			ExportStateMachineDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
	public void doExportStateMachinePositiveClick(String string, Object selectedItem) {
		StateMachine stateMachine = (StateMachine) selectedItem;
		    File root = Environment.getExternalStorageDirectory();
		    File folder = new File(root.getAbsolutePath()+"/"+AppData.APPLICATION_FOLDER+"/"+AppData.STATEMACHINEFOLDER +"/");
		    folder.mkdirs();
		    File exportfile = new File(folder, string);
		    ObjectOutputStream outputStream = null;
		    if (root.canWrite()){
		    	try {
					outputStream = new ObjectOutputStream(new FileOutputStream(exportfile));
					outputStream.writeObject(stateMachine);
		        } catch (FileNotFoundException e) {
		        	e.printStackTrace();
		        	Toast.makeText(this, "Sorry, FileNotFoundException occured.", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
					 Toast.makeText(this, "Sorry, an error occured.", Toast.LENGTH_LONG).show();
				}
            
		        
		    }
		    else{
		    	Toast.makeText(this, "Sorry, location "+root+" is write protected.", Toast.LENGTH_LONG).show();
		    }
            //Close the ObjectOutputStream
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(this, "Statemachine saved as: " + exportfile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Sorry, an error occured.", Toast.LENGTH_LONG).show();
            }
        }
}
