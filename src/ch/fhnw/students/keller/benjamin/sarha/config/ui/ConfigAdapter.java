package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class ConfigAdapter extends BaseAdapter {
	private Config config;
	private Context context;
	private int position;

	private enum Viewtypes {
		HEADER, IO
	};

	private LayoutInflater inflater;

	public ConfigAdapter(Context context, Config config) {
		this.config = config;
		this.context =context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public int getCount() {

		return config.countH();
	}

	@Override
	public Object getItem(int position) {
		int counter = position;
		for (IO.Type type : config.types) {
			if(config.count(type)==0){
				continue;
			}
			
			if (counter == 0) {
				//its a Header
				return IO.typeHeaders.get(type);
			}
			if (config.count(type) >= counter) {
				//its an item
				return config.ios.get(type).get(counter - 1);
			} else {
				counter = counter - config.countH(type);
			}

		}
		/*for (IO.Type type : IO.Type.values()) {
			if (position == 0) {
				return IO.typeHeaders.get(type);
			}
			if (config.count(type) >= position) {
				return config.ios.get(type).get(position - 1);
			} else {
				position = position - config.countH(type);
			}

		}*/

		return null;
	}
	@Override
	public boolean areAllItemsEnabled(){
		return false;
	}
	@Override 
	public boolean isEnabled(int position){		
		Object item = getItem(position);
		if (item instanceof IOs) {
			return true;
		} 
		
			return false;
				
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		this.position=position;
		int type = getItemViewType(position);
		System.out.println("getView " + position + " " + convertView
				+ " type = " + type);
		if (convertView == null) {
			holder = new ViewHolder();
			switch (Viewtypes.values()[type]) {
			case IO:
				convertView = inflater.inflate(R.layout.config_listviewitem_io, null);
				System.out.println("convertview" +ConfigAdapter.this.position);
				convertView.setOnTouchListener(holder.delListener);
				//convertView.setOnClickListener(holder.clickListener);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textView1);
				
				holder.textSmall = (TextView) convertView.findViewById(R.id.textView2);
				
				break;
			case HEADER:
				convertView = inflater.inflate(R.layout.config_listviewitem_header, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textView1);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		switch (Viewtypes.values()[type]) {
		case IO:
			IOs io = (IOs) getItem(position);
			holder.textView.setText(io.name);
			if(io.address!=null){
			holder.textSmall.setText(io.address.toString());
			}
			ArrayList<AddressIdentifier> assigned =config.getAssignedAddresses();
			assigned.remove(io.address);
			if(assigned.contains(io.address)){
				holder.textSmall.setTextColor(Color.RED);
			}
			else{
				holder.textSmall.setTextColor(Color.GRAY);
			}
			holder.delListener.position=position;
			
			break;
		case HEADER:

			holder.textView.setText((CharSequence) getItem(position));
			break;
		}
		
		return convertView;

	}

	public void remove(IOs io) {
		config.remove(io);
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return Viewtypes.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		Object item = getItem(position);
		if (item instanceof IOs) {
			return Viewtypes.IO.ordinal();
		} else {
			return Viewtypes.HEADER.ordinal();
		}
	}

	public class ViewHolder {
		public TextView textView;
		public TextView textSmall;
		public DeleteMotionListener delListener = new DeleteMotionListener() {
			@Override
			void delete(int position) {
				Toast.makeText(context, "Deleted "+getItem(position) , 200).show();
				remove((IOs) getItem(position));
				
			}

			@Override
			void onClick(int position) {
				System.out.println("onclick");
				((ConfigActivity)context).showDialog(getItem(position),false);
				
			}
		};
		
	}

	public void add(IOs io) {
		config.add(io);
		notifyDataSetChanged();

	}
}
