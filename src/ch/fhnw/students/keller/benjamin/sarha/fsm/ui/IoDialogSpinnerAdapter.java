package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;
/**SpinnerAdapter for a specific IO Type
 * 
 * 
 * 
 * @author Benjamin Keller
 *
 */
class IoDialogSpinnerAdapter extends BaseAdapter {
	private ArrayList<IOs> ios = new ArrayList<IOs>();
	private Context context;
	
	/**Creates a new IoDialogSpinnerAdapter for a specific IO Type
	 * 
	 * @param context Android context to get the LayoutInflater from
	 * @param config Configuration of which the items of the spinner are taken
	 * @param type IO Type of the items
	 */
	public IoDialogSpinnerAdapter(Context context, Config config, IO.Type type) {
		ios = config.getListOfType(type);
		this.context=context;
	}

	@Override
	public int getCount() {
		return ios.size();
	}

	@Override
	public Object getItem(int position) {
		if (ios.get(position) != null) {
			return ios.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(android.R.layout.simple_spinner_dropdown_item, null);
		} else {
			v = convertView;
		}
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(((IOs) getItem(position)).name);//Set the displayed string of an item to the IOs name as defined in the configuration
		return v;	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(android.R.layout.simple_spinner_item, null);
		} else {
			v = convertView;
		}
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(((IOs) getItem(position)).name);//Set the displayed string of an item to the IOs name as defined in the configuration
		return v;
	}
	/**Gets the position of an item in the data list of the spinner identified by its AddressIdentifier
	 * 
	 * @param ai AdressIdentifier of the IO in the data list of the spinner
	 * @return Position of the IO within the spinner's data list
	 */
	public int getPosition(AddressIdentifier ai) {
		for (IOs io : ios) {
			if (io.address == ai) {
				return ios.indexOf(io);
			}
		}
		return -1;
	}

}

