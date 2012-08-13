package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.Portable;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;

public class ConfigSpinnerAdapter extends ArrayAdapter<Portable> {
	
	
	public ConfigSpinnerAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.add(IO.defaultConfig());
		for (Portable config : AppData.data.configs) {
			this.add(config);
		}
	}

}
