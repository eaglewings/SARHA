package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.ArrayList;

import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ConfigsAdapter extends ArrayAdapter<Config> implements ListAdapter {
	private Context context;
	
	public ConfigsAdapter(Context context, ArrayList<Config> configs){
		super(context, android.R.layout.simple_list_item_2, configs);
		this.context=context;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Config config=  getItem(position);
		if (v == null) {
			

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
			

		}
		TextView txtView1 = (TextView) v.findViewById(android.R.id.text1);
		TextView txtView2 = (TextView) v.findViewById(android.R.id.text2);
		txtView1.setText(config.name);
		txtView2.setText(mktext2(config));
		return v;
	}


	private String mktext2(Config config) {
		String ret="";
		for (IO.Type type : IO.Type.values()){
			if(config.count(type)>0){
				if(!ret.equals("")){
					ret=ret+", ";
				}
				ret= ret+config.count(type)+type.name();
			}
		}
		return ret;
	}
	
	
	
}
