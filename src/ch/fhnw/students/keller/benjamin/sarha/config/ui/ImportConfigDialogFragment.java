package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.ConfigImporter;
import ch.fhnw.students.keller.benjamin.sarha.ConfigImporter.CompareResult;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class ImportConfigDialogFragment extends DialogFragment {
	private Config importedConfig, matchedConfig;
	private Button btOk, btCancel;
	private Spinner spConfig;
	private TextView tvConfigName, tvConfigId, tvTypeofConflict,
			tvConflictingName, tvConflictingId, tvNewName, tvProgress;
	private EditText etNewName;
	private ProgressBar pbDownload;
	private ConfigImporter.CompareResult resultOfSelected;
	private String name;
	private int createId;
	private int changeId;
	private int pbMax = 0, pbProgress = 0;
	private boolean success;
	private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);

	public static ImportConfigDialogFragment newInstance(String name,
			int createId, int changeId) {
		ImportConfigDialogFragment f = new ImportConfigDialogFragment();
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
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
		pbDownload = (ProgressBar) v.findViewById(R.id.pbDownload);
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
					ImportConfigDialogFragment.this.dismiss();
					break;
				case NO_MATCH:
					AppData.data.configs.add(importedConfig);
					ImportConfigDialogFragment.this.dismiss();
					break;
				case OVERWRITE:
					int index = AppData.data.configs.indexOf(matchedConfig);
					AppData.data.configs.remove(matchedConfig);
					AppData.data.configs.add(index, importedConfig);
					ImportConfigDialogFragment.this.dismiss();
					break;
				case RENAME:
					String newName = etNewName.getText().toString().trim();
					if (Utils.checkConfigName(newName)) {
						importedConfig.name = newName;
						AppData.data.configs.add(importedConfig);
						ImportConfigDialogFragment.this.dismiss();
					} else {
						Toast.makeText(
								ImportConfigDialogFragment.this.getActivity(),
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
								ImportConfigDialogFragment.this.getActivity(),
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
			getDialog().setTitle("Download config form control unit");
			spConfig.setVisibility(View.GONE);
			setViews();
			if(resultOfSelected!=ConfigImporter.CompareResult.NONE && resultOfSelected!=ConfigImporter.CompareResult.MATCH){
			btOk.setEnabled(false);
			tvProgress.setVisibility(View.VISIBLE);
			pbDownload.setVisibility(View.VISIBLE);
			final Thread downloadThread = new Thread() {
				public void run() {
					System.out.println("uploadThread start");
					importedConfig = CommManager.protocol.getConfig(queue);
					if(importedConfig!=null){
						success=true;
					}
					System.out.println("uploadThread stop");
				}
			};
			Thread uiUpdateThread = new Thread() {
				private boolean pbMaxSet;

				public void run() {
					System.out.println("uiUpdateThread start");
					AppData.data.handler.post(new Runnable() {
						@Override
						public void run() {
							tvProgress.setVisibility(View.VISIBLE);
							pbDownload.setVisibility(View.VISIBLE);
							btOk.setEnabled(false);
						}
					});
					while (downloadThread.isAlive()) {
						
						AppData.data.handler.post(new Runnable() {

							@Override
							public void run() {
								if (queue.peek() != null) {
									int val = queue.poll();
									System.out.println("uiUpdateThread updateui value: " + val);
									if (!pbMaxSet) {
										System.out.println("uiUpdateThread set size " + val);
										pbMax = val;
										pbDownload.setMax(val);
										pbMaxSet = true;
									} else {
										System.out.println("uiUpdateThread set Progress " + val);
										pbDownload.setProgress(val);
										pbProgress=val;
									}
									tvProgress.setText(pbProgress
											+ " / " + pbMax
											+ " Bytes done");
								}
							}
						});
					}
					if (success) {
						AppData.data.handler.post(new Runnable() {

							@Override
							public void run() {

								btOk.setEnabled(true);
							}
						});
					} else {
						AppData.data.handler.post(new Runnable() {

							@Override
							public void run() {

								tvConflictingName.setText("Download failed");
							}
						});

					}
					System.out.println("uiUpdateThread stop");
				}
			};
			downloadThread.start();
			uiUpdateThread.start();
			}
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
		tvProgress.setVisibility(View.GONE);
		pbDownload.setVisibility(View.GONE);
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
