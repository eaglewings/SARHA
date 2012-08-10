package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.util.concurrent.ArrayBlockingQueue;

import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.remote.ui.DevicesDialogFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class UploadStateMachineDialogFragment extends DialogFragment {

	private ProgressBar pbUpload;
	private Button btOk, btCancel;
	private TextView tvInfo, tvProgress, tvStateMachineDeviceName, tvStateMachineDeviceId;;
	private StateMachine stateMachine;
	private int pbMax = 0, pbProgress = 0;
	private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(
			1);

	public static UploadStateMachineDialogFragment newInstance(StateMachine stateMachine) {
		UploadStateMachineDialogFragment f = new UploadStateMachineDialogFragment();
		f.stateMachine = stateMachine;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fsm_dialog_upload, container);
		pbUpload = (ProgressBar) v.findViewById(R.id.pbUpload);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		tvStateMachineDeviceId = (TextView) v.findViewById(R.id.tvStateMachineDeviceId);
		tvStateMachineDeviceId.setText(Utils.idString(stateMachine.createId, stateMachine.getChangeId()));
		tvStateMachineDeviceName = (TextView) v.findViewById(R.id.tvStateMachineDeviceName);
		tvStateMachineDeviceName.setText(stateMachine.getName());
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
						success = CommManager.protocol.setStateMachine(stateMachine, queue);
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
								tvInfo.setText("Uploading ...");
								pbUpload.setVisibility(View.VISIBLE);
								btOk.setEnabled(false);
							}
						});
						while (uploadThread.isAlive()) {
							
							AppData.data.handler.post(new Runnable() {

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
											pbProgress=val;
											if(pbProgress> val){
												pbMaxSet = false; //reset when upload of lua script starts... ObjectStream of StateMachine is transmitted first. See setStateMachine(..) in Protocol.java
											}
										}
										tvProgress.setText(pbProgress
												+ " / " + pbMax
												+ " Bytes done");
									}
								}
							});
						}
						if (success) {
							getDialog().dismiss();
						} else {
							AppData.data.handler.post(new Runnable() {

								@Override
								public void run() {

									tvInfo.setText("Upload failed");
								}
							});

						}
						System.out.println("uiUpdateThread stop");
					}
				};
				if(CommManager.connectedDevice!=null){
				uploadThread.start();
				uiUpdateThread.start();
				}
				else{
					DevicesDialogFragment.newInstance(null,null).show(
							getFragmentManager(), "dialog");
				}
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				
			}
		});
		getDialog().setTitle("Upload StateMachine");
		if(CommManager.connectedDevice==null){
		DevicesDialogFragment.newInstance(null,null).show(
				getFragmentManager(), "dialog");
		}
		return v;
	}
}
