package ch.fhnw.students.keller.benjamin.sarha.settings.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.comm.Device;

public class DeviceAdapter extends BaseAdapter implements Observer{
	private ArrayList<Device> devices= AppData.data.devices;
	private LayoutInflater inflater;
	public DeviceAdapter(Context context){
		CommManager.addDeviceListObserver(this);
		inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public DeviceAdapter(Context context, ArrayList<Device> devices){
		inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.devices=devices;
	}
	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int pos) {
		return devices.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder = new ViewHolder();
		final Device device= (Device) getItem(position);
		holder.device=device;
		View v = convertView;
		
		if(v==null){
			v = inflater.inflate(R.layout.settings_listviewitem_device, null);
		}
		holder.tvName = (TextView) v.findViewById(R.id.tvName);
		holder.tvIp = (TextView) v.findViewById(R.id.tvIp);
		holder.ivState = (ImageView) v.findViewById(R.id.ivState);
		switch (device.getState()){
		case CONNECTABLE:
			holder.ivState.setImageResource(R.drawable.settings_device_connectable);
			break;
		case OFFLINE:
			holder.ivState.setImageResource(R.drawable.settings_device_offline);
			break;
		case ONLINE:
			holder.ivState.setImageResource(R.drawable.settings_device_online);
			break;
		case UNKNOWN:
			holder.ivState.setImageResource(R.drawable.settings_device_unknown);
			break;
		}
		holder.tvName.setText(device.getName());
		final Handler handler = new Handler();
		new Thread() {
			
			@Override
			public void run() {
				final String hostname= device.getAddress().getHostName();
				final int port =device.getAddress().getPort();
				handler.post(new Runnable() {
			
			@Override
			public void run() {
				holder.tvIp.setText(hostname + ":"
				+ port);
			}
		});
			}
		}.start();
		
		
		
		return v;
	}
	public void setDevices(ArrayList<Device> devices){
		this.devices=devices;
		notifyDataSetChanged();
	}

	@Override
	public void update(Observable observable, Object data) {
		this.notifyDataSetChanged();
		
	}
	
	private class ViewHolder {
		public Device device;
		public TextView tvName, tvIp;
		public ImageView ivState;
	}

}
