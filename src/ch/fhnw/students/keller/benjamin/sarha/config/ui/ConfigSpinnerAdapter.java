package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class ConfigSpinnerAdapter extends ArrayAdapter<Config> {

	public ConfigSpinnerAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item,
				android.R.id.text1, AppData.configs);
	}

}
