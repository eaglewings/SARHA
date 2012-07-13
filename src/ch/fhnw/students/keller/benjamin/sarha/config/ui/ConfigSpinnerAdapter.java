package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;

public class ConfigSpinnerAdapter extends ArrayAdapter<Config> {
	static ArrayList<Config> configs= new ArrayList<Config>(AppData.data.configs);
	static{
		configs.add(0, IO.defaultConfig());
	}
	public ConfigSpinnerAdapter(Context context) {
		
		super(context, android.R.layout.simple_spinner_item,
				android.R.id.text1, configs);
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
	}

}
