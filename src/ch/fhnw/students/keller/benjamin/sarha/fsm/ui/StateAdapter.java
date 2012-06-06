package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

public class StateAdapter extends ArrayAdapter<Transition> {
	private Context context;
	public StateAdapter(Context context,State state){
		super(context,android.R.layout.simple_list_item_2,android.R.id.text1,state);
		this.context=context;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			Log.d("StateAdapter", "v==null");
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("StateAdapter", "LayoutInflater");
			v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
			Log.d("StateAdapter", "v= inflated");
		}
		Log.d("StateAdapter", "position: "+position);
		Transition t = this.getItem(position);
		Log.d("StateAdapter", "t: " + t.toString());
		if (t != null) {
			Log.d("StateAdapter", "t!=null");
			TextView txtView1 = (TextView) v.findViewById(android.R.id.text1);
			TextView txtView2 = (TextView) v.findViewById(android.R.id.text2);
			Log.d("StateAdapter", "Textviews");
			if (txtView1 != null) {
				txtView1.setText(t.toString());
				Log.d("StateAdapter", "tv1");
			}
			if (txtView2 != null) {
				Log.d("StateAdapter", "tv2");
				if (t.getToState()!=null){
					txtView2.setText(t.getToState().getStateName());
				}
				else{
					txtView2.setText("no TO state defined");
				}
				Log.d("StateAdapter", "tv2");
			}
		}
		Log.d("StateAdapter", "fertig v: "+v.toString());
		return v;
	}
}
