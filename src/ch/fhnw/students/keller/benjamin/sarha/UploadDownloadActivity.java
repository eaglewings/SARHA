package ch.fhnw.students.keller.benjamin.sarha;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.Importer.PortableType;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.remote.ui.DevicesDialogFragment;

public class UploadDownloadActivity extends FragmentActivity {
	private ListView lvPortable;
	private TextView tvName, tvId, tvOnApp, tvPortable;
	private BaseAdapter adapter;
	private String name;
	private int createId, changeId;
	private PortableType type;

	private OnClickListener downloadClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (tvName.getText().toString().equals("No device connected")) {
				DevicesDialogFragment.newInstance(null, null).show(getSupportFragmentManager(), "dialog");
			} else {
				ImportDialogFragment.newInstance(type, name, createId, changeId).show(getSupportFragmentManager(), "dialog");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = AppData.uploadDownloadPortableType;
		setContentView(R.layout.activity_upload_download);
		tvPortable = (TextView) findViewById(R.id.tvPortable);
		tvPortable.setText(type.labelUC+" on Device");
		lvPortable = (ListView) findViewById(R.id.lvPortable);
		tvId = (TextView) findViewById(R.id.tvId);
		tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText("No device connected");
		tvName.setOnClickListener(downloadClickListener);
		tvId.setVisibility(View.GONE);
		tvId.setOnClickListener(downloadClickListener);
		tvOnApp = (TextView) findViewById(R.id.tvOnApp);
		tvOnApp.setText(type.labelUC+"s on App");
		adapter = type.getListAdapter(this);
		lvPortable.setAdapter(adapter);

		lvPortable.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				UploadDialogFragment.newInstance((Portable) lvPortable.getItemAtPosition(pos)).show(getSupportFragmentManager(), "dialog");

			}
		});

	}

	@Override
	protected void onResume() {
		if (CommManager.connectedDevice == null) {
			DevicesDialogFragment.newInstance(null, null).show(getSupportFragmentManager(), "dialog");
		}
		super.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (CommManager.connectedDevice != null) {
				String id[] = CommManager.protocol.getId(type);
				if (id != null) {
					if (id[0] != null && id[1] != null && id[2] != null) {
						name = id[0];
						createId = Integer.parseInt(id[1]);
						changeId = Integer.parseInt(id[2]);
						if (name.equals("0")) {
							tvName.setText("No " + type.labelLC + " on device \"" + CommManager.connectedDevice.getName() + "\"");
						} else {
							tvName.setText(name);
							tvId.setText(Utils.idString(createId, changeId));
							tvId.setVisibility(View.VISIBLE);
						}

					}
				}
			}
		}
		adapter.notifyDataSetChanged();
		super.onWindowFocusChanged(hasFocus);
	}
}
