package ch.fhnw.students.keller.benjamin.sarha.config.ui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class ConfigManagerActivity extends FragmentActivity {
	private ConfigManagerAdapter adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_activity_configmanager);
		
		ListView list = (ListView) findViewById(R.id.listView1);
		adapter= new ConfigManagerAdapter(this, AppData.configs);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(view.getContext(),ConfigActivity.class);
				i.putExtra("ConfigIndex", position);
				startActivity(i);
				
			}
		});
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.config_configmanager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menuitemnewconfig) {
			showMyDialog(item.getItemId());
			return true;
		} else if (item.getItemId() == R.id.menuitemexportconfig) {
			showMyDialog(item.getItemId());
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	void showMyDialog(int id) {
		DialogFragment newFragment =null;
		if (id == R.id.menuitemnewconfig) {
			newFragment = NewConfigDialogFragment.newInstance();
		} else if (id == R.id.menuitemexportconfig) {
			newFragment = ExportConfigDialogFragment.newInstance();
			Log.d("export", "export");
		}
	    if(newFragment!=null){
	    newFragment.show(getSupportFragmentManager(), "dialog");
	    }
	}

	public void doNewConfigPositiveClick(String name) {
		
		adapter.add(new Config(name));
		
	}

	public void doNewConfigNegativeClick() {
		// TODO Auto-generated method stub
		
	}

	public void doExportConfigPositiveClick(String string, Object selectedItem) {
		Log.d("managerApp", "doexport");
			
		Config config = (Config) selectedItem;
		    File root = Environment.getExternalStorageDirectory();
		    File exportfile = new File(root, config.name+".cfg");
		    ObjectOutputStream outputStream = null;
		    if (root.canWrite()){
		    	Log.d("managerApp", "can write to: "+exportfile);
		        try {
					outputStream = new ObjectOutputStream(new FileOutputStream(exportfile));
					outputStream.writeObject(config);
		        } catch (FileNotFoundException e) {
		        	Log.d("managerApp", "error1");
					e.printStackTrace();
				} catch (IOException e) {
					Log.d("managerApp", "error2");
					e.printStackTrace();
				}
            
		        
		    }
		
        
            //Close the ObjectOutputStream
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	

}