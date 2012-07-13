package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.ArrayList;

import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AddressIdentifierAdapter extends ArrayAdapter<AddressIdentifier> {
	private Context context;
	private Config config;
	private IOs io;
	public AddressIdentifierAdapter(Context context, int textViewResourceId, IOs io,
			ArrayList<AddressIdentifier> hwAddresses) {
		super(context, textViewResourceId, hwAddresses);
		this.context=context;
		config=((ConfigActivity)context).config;
		this.io=io;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if(convertView==null){
			v=((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_spinner_item, null);
		}
		else{
			v=convertView;
		}
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(getItem(position).toString());
		colorize(tv,position);
		
		return tv;
	}
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v;
 
		if (null == convertView) {
			v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_spinner_dropdown_item, null);
		} else {
			v  = convertView;
		}
 
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(getItem(position).toString());
		colorize(tv,position);
		
		return v;
	}
	
	private void colorize(TextView tv, int position){
		ArrayList<AddressIdentifier> assigned =config.getAssignedAddresses();
		assigned.remove(io.address);
		if(assigned.contains(getItem(position))){
			System.out.println("3: "+getItem(position)+" pos: "+position);
			tv.setTextColor(Color.RED);
		}
		else{
			tv.setTextColor(Color.BLACK);
		}
		
	}
}
