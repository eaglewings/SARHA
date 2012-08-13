package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.ArrayList;
import java.util.Date;

import ch.fhnw.students.keller.benjamin.sarha.Portable;
import ch.fhnw.students.keller.benjamin.sarha.Utils;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ConfigManagerAdapter extends BaseAdapter {
	private ArrayList<? extends Portable> configs;
	private LayoutInflater inflater;
	
	public ConfigManagerAdapter(Context context, ArrayList<? extends Portable> configs){
		this.configs=configs;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return configs.size();
	}

	@Override
	public Object getItem(int position) {
		return configs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(android.R.layout.simple_list_item_2, null);
		TextView tvMedium = (TextView) v.findViewById(android.R.id.text1);
		TextView tvSmall = (TextView) v.findViewById(android.R.id.text2);
		
		
		Config config = (Config) getItem(position);
		tvMedium.setText(config.getName());
		tvSmall.setText(Utils.idString(config.getCreateId(), config.getChangeId()));
		
		return v;
	}
	
	
	
}
