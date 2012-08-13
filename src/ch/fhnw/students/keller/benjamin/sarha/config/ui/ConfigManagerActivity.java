package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.content.Intent;
import android.os.Bundle;
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
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.ExportDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.ImportDialogFragment;
import ch.fhnw.students.keller.benjamin.sarha.Importer.PortableType;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.UploadDownloadActivity;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class ConfigManagerActivity extends FragmentActivity {
	private ConfigManagerAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_activity_configmanager);

		ListView list = (ListView) findViewById(R.id.listView1);

		adapter = new ConfigManagerAdapter(this, AppData.data.configs);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(view.getContext(), ConfigActivity.class);
				i.putExtra("ConfigIndex", position);
				startActivity(i);

			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
		if (hasFocus) {
			adapter.notifyDataSetChanged();
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitemnewconfig:
			showMyDialog(item.getItemId());
			break;
		case R.id.menuitemexportconfig:
			showMyDialog(item.getItemId());
			break;
		case R.id.menuitemimportconfig:
			showMyDialog(item.getItemId());
			break;
		case R.id.menuitemdownloadconfig:
			AppData.uploadDownloadPortableType = PortableType.CONFIG;
			Intent intent = new Intent(this, UploadDownloadActivity.class);
			startActivity(intent);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	void showMyDialog(int id) {
		DialogFragment newFragment = null;
		switch (id) {
		case R.id.menuitemnewconfig:
			newFragment = NewConfigDialogFragment.newInstance(null);
			break;
		case R.id.menuitemexportconfig:
			newFragment = ExportDialogFragment.newInstance(PortableType.CONFIG);
			break;
		case R.id.menuitemimportconfig:
			newFragment = ImportDialogFragment.newInstance(PortableType.CONFIG, null, 0, 0);
			break;
		default:
			break;
		}
		if (newFragment != null) {
			newFragment.show(getSupportFragmentManager(), "dialog");
		}
	}

	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}

}