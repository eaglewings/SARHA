package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import java.net.InetSocketAddress;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceModel;
import ch.fhnw.students.keller.benjamin.sarha.comm.EasyComm;
import ch.fhnw.students.keller.benjamin.sarha.comm.Protocol;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;

public class RemoteActivity extends Activity implements Observer {
	private Config cfg = IO.defaultConfig();
	private Button btProg, btDebug;
	private DeviceModel device = new DeviceModel(cfg);
	private LinearLayout lla;
	private ViewStock viewStock;
	private Protocol protocol;
	private EasyComm comm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_activity);

		device.addObserver(this);
		comm = new EasyComm();
		protocol= comm.connect(new InetSocketAddress("192.168.0.105", 2000));
		viewStock = new ViewStock(this, device, protocol);
		
		lla = (LinearLayout) findViewById(R.id.linearLayout3);
		viewStock.addViews(lla);
		

		btProg = (Button) findViewById(R.id.button1);
		if (device.isProgramRunning()) {
			btProg.setText("Prog stop");
		} else {
			btProg.setText("Prog run");
		}
		btProg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (btProg.getText().equals("Prog stop")) {
					device.programStop();
				} else {
					device.programRun();
				}

			}
		});
		//TODO Uncomment following line
		//btDebug = (Button) findViewById(R.id.button2);
		btDebug.setText("Debug");
		if (device.isProgramRunning()) {
			btDebug.setEnabled(true);
		} else {
			btDebug.setEnabled(false);
		}
		btDebug.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (device.isDebug()) {
					device.debugInactive();
				} else {
					device.debugActive();
				}

			}
		});
		
		
		
	
			  new Thread(new Runnable() {
			    public void run() {
			    	while (true){
			    		try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			     
			        	
			          //device.setIOvalue(IO.AnalogIn.AI0, protocol.getAddressValue());
			          
			        }
			      }
			    
			  }).start();
			
	}

	@Override
	public void update(Observable observable, Object data) {
		if (device.isProgramRunning()) {
			btProg.setText("Prog stop");
			btDebug.setEnabled(true);
		} else {
			btProg.setText("Prog run");
			btDebug.setEnabled(false);
		}

	}
	public void onDestroy(){
		super.onDestroy();
		comm.disconnect();
	}
	@Override
	protected void onPause() {
		AppData.saveAppData();
		super.onPause();
	}
}