package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.io.File;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.ConfigImporter;
import ch.fhnw.students.keller.benjamin.sarha.ConfigImporter.CompareResult;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class DownloadUploadConfigDialogFragment extends DialogFragment {
	private Config importedConfig, matchedConfig;
	private Button btOk, btCancel;
	private Spinner spConfig;
	private TextView tvConfigName, tvConfigId, tvTypeofConflict,
			tvConflictingName, tvConflictingId, tvNewName;
	private EditText etNewName;
	private ConfigImporter.CompareResult resultOfSelected;
	private String name;
	private int createId;
	private int changeId;

	public static DownloadUploadConfigDialogFragment newInstance(String name,
			int createId, int changeId) {
		DownloadUploadConfigDialogFragment f = new DownloadUploadConfigDialogFragment();
		f.name = name;
		f.createId = createId;
		f.changeId = changeId;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.config_dialog_import, container);
		etNewName = (EditText) v.findViewById(R.id.etNewName);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		spConfig = (Spinner) v.findViewById(R.id.spConfig);
		tvConfigName = (TextView) v.findViewById(R.id.tvConfigName);
		tvConfigId = (TextView) v.findViewById(R.id.tvConfigId);
		tvTypeofConflict = (TextView) v.findViewById(R.id.tvTypeofConflict);
		tvConflictingName = (TextView) v.findViewById(R.id.tvConflictingName);
		tvConflictingId = (TextView) v.findViewById(R.id.tvConflictingId);
		tvNewName = (TextView) v.findViewById(R.id.tvNewName);

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				switch (resultOfSelected) {
				case MATCH:
					DownloadUploadConfigDialogFragment.this.dismiss();
					break;
				case NO_MATCH:
					AppData.data.configs.add(importedConfig);
					DownloadUploadConfigDialogFragment.this.dismiss();
					break;
				case OVERWRITE:
					int index = AppData.data.configs.indexOf(matchedConfig);
					AppData.data.configs.remove(matchedConfig);
					AppData.data.configs.add(index, importedConfig);
					DownloadUploadConfigDialogFragment.this.dismiss();
					break;
				case RENAME:
					String newName = etNewName.getText().toString().trim();
					if (Utils.checkConfigName(newName)) {
						importedConfig.name = newName;
						AppData.data.configs.add(importedConfig);
						DownloadUploadConfigDialogFragment.this.dismiss();
					} else {
						Toast.makeText(
								DownloadUploadConfigDialogFragment.this.getActivity(),
								"New name invalid or in use", Toast.LENGTH_LONG)
								.show();
					}
					break;
				default:

				}
			}
		});

		if (name == null) { // Importing Config from SD card
			getDialog().setTitle("Import config from SD");
			spConfig.setVisibility(View.VISIBLE);
			File[] files;
			String[] fileNames;
			final File root = Environment.getExternalStorageDirectory();
			File folder = new File(root.getAbsolutePath() + "/"
					+ AppData.APPLICATION_FOLDER + "/" + AppData.CONFIGFOLDER
					+ "/");
			folder.mkdirs();
			files = folder.listFiles();
			fileNames = new String[files.length];
			int i = 0;
			for (File file : files) {
				fileNames[i] = file.getName();
				i++;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this.getActivity(), android.R.layout.simple_spinner_item,
					android.R.id.text1, fileNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spConfig.setAdapter(adapter);
			spConfig.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					File file = null;
					String fileName = (String) spConfig
							.getItemAtPosition(position);
					if (fileName != null && !fileName.equals("")) {
						file = new File(root.getAbsolutePath() + "/"
								+ AppData.APPLICATION_FOLDER + "/"
								+ AppData.CONFIGFOLDER + "/" + fileName);

						importedConfig = ConfigImporter.getFromFile(
								DownloadUploadConfigDialogFragment.this.getActivity(),
								file);
						if (importedConfig != null) {
							name = importedConfig.name;
							createId = importedConfig.createId;
							changeId = importedConfig.getChangeId();
							setViews();
						} else {
							tvTypeofConflict
									.setText("Selected file is not a Config");
							tvTypeofConflict.setVisibility(View.VISIBLE);
							tvConfigName.setVisibility(View.GONE);
							tvConfigId.setVisibility(View.GONE);
							tvConflictingName.setVisibility(View.GONE);
							tvConflictingId.setVisibility(View.GONE);
							etNewName.setVisibility(View.GONE);
							tvNewName.setVisibility(View.GONE);
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});

		} else {
			getDialog().setTitle("Import config form control unit");
			spConfig.setVisibility(View.GONE);
			setViews();

		}

		return v;
	}

	private void setViews() {
		tvConfigName.setText(name);
		tvConfigId.setText("created: "
				+ (new Date((long) createId * 1000)).toLocaleString()
				+ " changed: "
				+ (new Date((long) changeId * 1000)).toLocaleString());

		matchedConfig = ConfigImporter.getMatchedConfig(name, createId,
				changeId);
		resultOfSelected = ConfigImporter
				.getMatchType(name, createId, changeId);
		if (resultOfSelected != CompareResult.NONE
				&& resultOfSelected != CompareResult.NO_MATCH) {
			tvConflictingName.setText(matchedConfig.name);
			tvConflictingId.setText("created: "
					+ (new Date((long) matchedConfig.createId * 1000))
							.toLocaleString()
					+ " changed: "
					+ (new Date((long) matchedConfig.getChangeId() * 1000))
							.toLocaleString());
		}
		tvConfigName.setVisibility(View.VISIBLE);
		tvConfigId.setVisibility(View.VISIBLE);
		tvTypeofConflict.setVisibility(View.VISIBLE);
		tvConflictingName.setVisibility(View.VISIBLE);
		tvConflictingId.setVisibility(View.VISIBLE);
		etNewName.setVisibility(View.GONE);
		tvNewName.setVisibility(View.GONE);
		switch (resultOfSelected) {
		case MATCH:
			tvTypeofConflict.setText("Config already exists");
			
			break;
		case NO_MATCH:
			tvTypeofConflict.setText("No conflicts detected");
			tvConflictingName.setVisibility(View.GONE);
			tvConflictingId.setVisibility(View.GONE);
			
			break;
		case OVERWRITE:
			if (matchedConfig.getChangeId() > changeId) {
				tvTypeofConflict
						.setText("Config exists in newer version. Overwrite?");
			} else {
				tvTypeofConflict
						.setText("Config exists in older version. Overwrite?");
			}
			break;
		case RENAME:
			tvTypeofConflict.setText("Name already exists: please rename");
			etNewName.setVisibility(View.VISIBLE);
			tvNewName.setVisibility(View.VISIBLE);
			break;
		case NONE:
			tvTypeofConflict.setText("No config on Device");
			tvConflictingName.setVisibility(View.GONE);
			tvConflictingId.setVisibility(View.GONE);
		}
	}

}
