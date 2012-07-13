package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;

public class StateMachineManagerAdapter extends BaseAdapter{
	private ArrayList<StateMachine> stateMachines;
	private LayoutInflater inflater;
	
	public StateMachineManagerAdapter(Context context, ArrayList<StateMachine> stateMachines){
		this.stateMachines=stateMachines;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return stateMachines.size();
	}

	@Override
	public Object getItem(int position) {
		return stateMachines.get(position);
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
		
		StateMachine stateMachine = (StateMachine) getItem(position);
		
		tvMedium.setText(stateMachine.getName());
		tvSmall.setText("created: "+(new Date((long)stateMachine.createId*1000)).toLocaleString()+"changed: "+(new Date((long)stateMachine.getChangeId()*1000)).toLocaleString());
		
		return v;
	}

}
