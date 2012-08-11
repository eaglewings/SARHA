package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.concurrent.ArrayBlockingQueue;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class UploadConfigDialogFragment extends DialogFragment {

	private ProgressBar pbUpload;
	private Button btOk, btCancel;
	private TextView tvInfo, tvProgress;
	private Config config;
	private int pbMax = 0, pbProgress = 0;
	private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);

	public static UploadConfigDialogFragment newInstance(Config config) {
		UploadConfigDialogFragment f = new UploadConfigDialogFragment();
		f.config = config;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.config_dialog_upload, container);
		pbUpload = (ProgressBar) v.findViewById(R.id.pbUpload);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		tvInfo = (TextView) v.findViewById(R.id.tvInfo);
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
		pbUpload.setMax(0);
		pbUpload.setVisibility(View.GONE);
		tvProgress.setVisibility(View.GONE);
		btOk.setOnClickListener(new OnClickListener() {
			private boolean success = false;

			@Override
			public void onClick(View v) {
				btOk.setEnabled(false);
				final Thread uploadThread = new Thread() {
					public void run() {
						System.out.println("uploadThread start");
						success = CommManager.protocol.setConfig(config, queue);
						System.out.println("uploadThread stop");
					}
				};
				Thread uiUpdateThread = new Thread() {
					private boolean pbMaxSet;

					public void run() {
						AppData.handler.post(new Runnable() {
							@Override
							public void run() {
								tvProgress.setVisibility(View.VISIBLE);
								pbUpload.setVisibility(View.VISIBLE);
								btOk.setEnabled(false);
							}
						});
						while (uploadThread.isAlive()) {

							AppData.handler.post(new Runnable() {

								@Override
								public void run() {
									if (queue.peek() != null) {
										int val = queue.poll();
										System.out.println("uiUpdateThread updateui value: " + val);
										if (!pbMaxSet) {
											System.out.println("uiUpdateThread set size " + val);
											pbMax = val;
											pbUpload.setMax(val);
											pbMaxSet = true;
										} else {
											System.out.println("uiUpdateThread set Progress " + val);
											pbUpload.setProgress(val);
											pbProgress = val;
										}
										tvProgress.setText(pbProgress + " / " + pbMax + " Bytes done");
									}
								}
							});
						}
						if (success) {
							getDialog().dismiss();
							getActivity().finish();
						} else {
							AppData.handler.post(new Runnable() {

								@Override
								public void run() {
									tvInfo.setText("Upload failed");
								}
							});

						}
					}
				};
				uploadThread.start();
				uiUpdateThread.start();

			}
		});
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();

			}
		});
		getDialog().setTitle("Upload Config");
		return v;
	}
}
