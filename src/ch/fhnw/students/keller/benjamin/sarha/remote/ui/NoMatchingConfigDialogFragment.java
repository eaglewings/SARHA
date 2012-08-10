package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.comm.Device;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.UploadDownloadConfigActivity;
import ch.fhnw.students.keller.benjamin.sarha.settings.ui.DeviceAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class NoMatchingConfigDialogFragment extends DialogFragment {
	private Button btOk, btCancel;
	private Thread offlineThread;
	public static NoMatchingConfigDialogFragment newInstance(Thread offlineThread) {
		NoMatchingConfigDialogFragment f = new NoMatchingConfigDialogFragment();
		f.offlineThread= offlineThread;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// CommManager.findDevices();

		View v = inflater.inflate(R.layout.remote_dialog_nomatchingconfig,
				container);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel=(Button) v.findViewById(R.id.btCancel);
		
		btOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						UploadDownloadConfigActivity.class);
				startActivity(i);
				dismiss();
				
				
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(offlineThread!=null){
					offlineThread.run();
				}
				dismiss();
				
			}
		});
		getDialog().setTitle("No matching config found in App");
		return v;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

}
