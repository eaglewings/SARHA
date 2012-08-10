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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
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
		adapter= new ConfigManagerAdapter(this, AppData.data.configs);
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
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewConfigDialogFragment.newInstance((Config) adapter.getItem(position)).show(getSupportFragmentManager(), "dialog");
				
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.config_configmanager, menu);
		return true;
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		adapter.notifyDataSetChanged();
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case  R.id.menuitemnewconfig:
			showMyDialog(item.getItemId());
			break;
		case  R.id.menuitemexportconfig:
			showMyDialog(item.getItemId());
			break;
		case  R.id.menuitemimportconfig:
			showMyDialog(item.getItemId());
			break;
		case  R.id.menuitemdownloadconfig:
			Intent intent = new Intent(this,UploadDownloadConfigActivity.class);
			startActivity(intent);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	void showMyDialog(int id) {
		DialogFragment newFragment =null;
		switch(id){
		case R.id.menuitemnewconfig:
			newFragment = NewConfigDialogFragment.newInstance(null);
			break;
		case R.id.menuitemexportconfig:
			newFragment = ExportConfigDialogFragment.newInstance();
			break;
		case R.id.menuitemimportconfig:
			newFragment= DownloadUploadConfigDialogFragment.newInstance(null,0,0);
			break;
		default:
			break;
		}
	    if(newFragment!=null){
	    newFragment.show(getSupportFragmentManager(), "dialog");
	    }
	}

	public void doNewConfigPositiveClick(String name) {
		adapter.add(new Config(name));
	}

	public void doNewConfigNegativeClick() {
	}

	public void doExportConfigPositiveClick(String string, Object selectedItem) {
		Config config = (Config) selectedItem;
		    File root = Environment.getExternalStorageDirectory();
		    File folder = new File(root.getAbsolutePath()+"/"+AppData.APPLICATION_FOLDER+"/"+AppData.CONFIGFOLDER +"/");
		    folder.mkdirs();
		    File exportfile = new File(folder, string);
		    ObjectOutputStream outputStream = null;
		    if (root.canWrite()){
		    	try {
					outputStream = new ObjectOutputStream(new FileOutputStream(exportfile));
					outputStream.writeObject(config);
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
                    Toast.makeText(this, "Config saved as: " + exportfile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Sorry, an error occured.", Toast.LENGTH_LONG).show();
            }
        }
	
	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
	
	
}