package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.comm.Device;
import ch.fhnw.students.keller.benjamin.sarha.settings.ui.DeviceAdapter;

public class DevicesDialogFragment extends DialogFragment implements Observer {
	private ListView lvDevices;
	private Button btOk, btCancel, btRefresh;
	private ProgressBar pbProgress;
	private ArrayList<Device> connectableDevices;
	private DeviceAdapter adapter;
	private Thread okThread, cancelThread;
	private boolean runProgressThread = false;
	

	public static DevicesDialogFragment newInstance(Thread okThread, Thread cancelThread) {
		DevicesDialogFragment f = new DevicesDialogFragment();
		CommManager.addDeviceListObserver(f);
		f.okThread = okThread;
		f.cancelThread = cancelThread;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// CommManager.findDevices();

		View v = inflater.inflate(R.layout.remote_dialog_devices, container);
		lvDevices = (ListView) v.findViewById(R.id.lvDevices);
		connectableDevices = Utils.getConnectableDevices();
		adapter = new DeviceAdapter(this.getActivity(), connectableDevices);
		lvDevices.setAdapter(adapter);
		lvDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CommManager.connect((Device) adapter.getItem(position));

			}
		});
		pbProgress = (ProgressBar) v.findViewById(R.id.pbProgress);
		pbProgress.setVisibility(View.GONE);
		btOk = (Button) v.findViewById(R.id.btOk);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		btRefresh = (Button) v.findViewById(R.id.btRefresh);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				if(okThread!=null){
					okThread.run(); // call of run instead of start is intentional here!
				}
			}
		});

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				if(cancelThread!= null){
					cancelThread.run();// call of run instead of start is intentional here!
				}
			}
		});

		btRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommManager.findDevices();

			}
		});
		
		getDialog().setTitle("Connect device");
		runProgressThread = true;
		new Thread() {
			public void run() {
				while (runProgressThread) {
					if((pbProgress.getVisibility()==View.VISIBLE && !CommManager.deviceFinder.isAlive()) || (pbProgress.getVisibility()==View.GONE && CommManager.deviceFinder.isAlive()))
					AppData.handler.post(new Runnable() {
						@Override
						public void run() {
							if(CommManager.deviceFinder.isAlive()){
								pbProgress.setVisibility(View.VISIBLE);
								btRefresh.setEnabled(false);
								
							}
							else{
								pbProgress.setVisibility(View.GONE);
								btRefresh.setEnabled(true);
							}
							
						}
					});
				}
			}}.start();
		
		
		return v;
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		runProgressThread = false;
		super.onDismiss(dialog);
	}

	@Override
	public void update(Observable observable, Object data) {
		System.out.println("Dialog update");
		connectableDevices = Utils.getConnectableDevices();
		adapter.setDevices(connectableDevices);
	}

}
