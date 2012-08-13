package ch.fhnw.students.keller.benjamin.sarha;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.Importer.CompareResult;
import ch.fhnw.students.keller.benjamin.sarha.Importer.PortableType;

public class ImportDialogFragment extends DialogFragment {
	private PortableType type;
	private Portable imported, matched;
	private Button btOk, btCancel;
	private Spinner spPortable;
	private TextView tvName, tvId, tvTypeofConflict,
			tvConflictingName, tvConflictingId, tvNewName, tvProgress, tvToBeImported;
	private EditText etNewName;
	private ProgressBar pbDownload;
	private CompareResult resultOfSelected;
	private String name;
	private int createId;
	private int changeId;
	private int pbMax = 0, pbProgress = 0;
	private boolean success;
	private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);
	
	public static ImportDialogFragment newInstance(PortableType type,String name,
			int createId, int changeId) {
		ImportDialogFragment f = new ImportDialogFragment();
		f.name = name;
		f.createId = createId;
		f.changeId = changeId;
		f.type =type;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_import, container, true);
		v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		etNewName = (EditText) v.findViewById(R.id.etNewName);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		spPortable = (Spinner) v.findViewById(R.id.spPortable);
		tvName = (TextView) v.findViewById(R.id.tvName);
		tvId = (TextView) v.findViewById(R.id.tvId);
		tvTypeofConflict = (TextView) v.findViewById(R.id.tvTypeofConflict);
		tvConflictingName = (TextView) v.findViewById(R.id.tvConflictingName);
		tvConflictingId = (TextView) v.findViewById(R.id.tvConflictingId);
		tvNewName = (TextView) v.findViewById(R.id.tvNewName);
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
		tvToBeImported = (TextView) v.findViewById(R.id.tvToBeImported);
		tvToBeImported.setText(type.labelUC+"to be imported");
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
					ImportDialogFragment.this.dismiss();
					break;
				case NO_MATCH:
					type.list.add(imported);
					ImportDialogFragment.this.dismiss();
					break;
				case OVERWRITE:
					int index = type.list.indexOf(matched);
					type.list.remove(matched);
					type.list.add(index, imported);
					ImportDialogFragment.this.dismiss();
					break;
				case RENAME:
					String newName = etNewName.getText().toString().trim();
					if (Utils.checkPortableName(type, newName)) {
						imported.setName(newName);
						type.list.add(imported);
						ImportDialogFragment.this.dismiss();
					} else {
						Toast.makeText(
								ImportDialogFragment.this.getActivity(),
								"New name invalid or in use", Toast.LENGTH_LONG)
								.show();
					}
					break;
				default:

				}
			}
		});

		if (name == null) { // Importing from SD card
			getDialog().setTitle("Import " +type.labelLC+ "from SD");
			spPortable.setVisibility(View.VISIBLE);
			File[] files;
			String[] fileNames;
			final File root = Environment.getExternalStorageDirectory();
			File folder = new File(root.getAbsolutePath() + "/"
					+ AppData.APPLICATION_FOLDER + "/" + type.folder
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
			spPortable.setAdapter(adapter);
			spPortable.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					File file = null;
					String fileName = (String) spPortable
							.getItemAtPosition(position);
					if (fileName != null && !fileName.equals("")) {
						file = new File(root.getAbsolutePath() + "/"
								+ AppData.APPLICATION_FOLDER + "/"
								+ type.folder + "/" + fileName);//TODO

						imported = Importer.getFromFile(
								ImportDialogFragment.this.getActivity(),
								file);
						if (imported != null) {
							name = imported.getName();
							createId = imported.getCreateId();
							changeId = imported.getChangeId();
							setViews();
						} else {
							tvTypeofConflict
									.setText("Selected file is not a "+type.labelLC);
							tvTypeofConflict.setVisibility(View.VISIBLE);
							tvName.setVisibility(View.GONE);
							tvId.setVisibility(View.GONE);
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
			getDialog().setTitle("Download "+type.labelLC+" form control unit");
			spPortable.setVisibility(View.GONE);
			setViews();
			if(resultOfSelected!=CompareResult.NONE && resultOfSelected!=CompareResult.MATCH){
			btOk.setEnabled(false);
			tvProgress.setVisibility(View.VISIBLE);
			pbDownload.setVisibility(View.VISIBLE);
			final Thread downloadThread = new Thread() {
				public void run() {
					System.out.println("uploadThread start");
					imported = Importer.getFromDevice(type, queue);//TODO
					if(imported!=null){
						if(imported.getPortableType()==type)
						success=true;
					}
					System.out.println("uploadThread stop");
				}
			};
			Thread uiUpdateThread = new Thread() {
				private boolean pbMaxSet;

				public void run() {
					System.out.println("uiUpdateThread start");
					AppData.handler.post(new Runnable() {
						@Override
						public void run() {
							tvProgress.setVisibility(View.VISIBLE);
							pbDownload.setVisibility(View.VISIBLE);
							btOk.setEnabled(false);
						}
					});
					while (downloadThread.isAlive()) {
						
						AppData.handler.post(new Runnable() {

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
						AppData.handler.post(new Runnable() {

							@Override
							public void run() {

								btOk.setEnabled(true);
							}
						});
					} else {
						AppData.handler.post(new Runnable() {

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
		tvName.setText(name);
		tvId.setText(Utils.idString(createId, changeId));

		matched = Importer.getMatchedPortable(type, name, createId, changeId);
		resultOfSelected = Importer
				.getMatchType(type, name, createId, changeId);
		if (resultOfSelected != CompareResult.NONE
				&& resultOfSelected != CompareResult.NO_MATCH) {
			tvConflictingName.setText(matched.getName());
			tvConflictingId.setText(Utils.idString(createId, changeId));
		}
		tvName.setVisibility(View.VISIBLE);
		tvId.setVisibility(View.VISIBLE);
		tvTypeofConflict.setVisibility(View.VISIBLE);
		tvConflictingName.setVisibility(View.VISIBLE);
		tvConflictingId.setVisibility(View.VISIBLE);
		etNewName.setVisibility(View.GONE);
		tvNewName.setVisibility(View.GONE);
		tvProgress.setVisibility(View.GONE);
		pbDownload.setVisibility(View.GONE);
		switch (resultOfSelected) {
		case MATCH:
			tvTypeofConflict.setText(type.labelUC+" already exists");
			
			break;
		case NO_MATCH:
			tvTypeofConflict.setText("No conflicts detected");
			tvConflictingName.setVisibility(View.GONE);
			tvConflictingId.setVisibility(View.GONE);
			
			break;
		case OVERWRITE:
			if (matched.getChangeId() > changeId) {
				tvTypeofConflict
						.setText(type.labelUC +" exists in newer version. Overwrite?");
			} else {
				tvTypeofConflict
						.setText(type.labelUC +" exists in older version. Overwrite?");
			}
			break;
		case RENAME:
			tvTypeofConflict.setText("Name already exists: please rename");
			etNewName.setVisibility(View.VISIBLE);
			tvNewName.setVisibility(View.VISIBLE);
			break;
		case NONE:
			tvTypeofConflict.setText("No "+type.labelLC+" on Device");
			tvConflictingName.setVisibility(View.GONE);
			tvConflictingId.setVisibility(View.GONE);
		}
	}

}
