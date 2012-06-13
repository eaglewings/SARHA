package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Action;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Action.ActionType;

public class ActionAdapter extends BaseAdapter {
	private ArrayList<ArrayList<Action>> actions;
	private LayoutInflater inflater;

	public ActionAdapter(Context context, ArrayList<ArrayList<Action>> actions) {
		this.actions = actions;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		int size = 0;
		for (ArrayList<Action> typeList : actions) {
			size += typeList.size();
		}
		return size;
	}

	@Override
	public Object getItem(int pos) {
		for (int i = 0; i < ActionType.values().length; i++) {
			for (ArrayList<Action> typeList : actions) {
				if (pos >= typeList.size()) {
					pos -= typeList.size();
				} else {
					return typeList.get(pos);
				}
			}
		}

		return null;
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return Action.ActionType.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		Action action = (Action) getItem(position);
		return Action.getType(action).ordinal();

	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		Action action = (Action) getItem(pos);
		holder.action = action;
		View v = convertView;
		System.out.println(action);
		switch (Action.ActionType.values()[getItemViewType(pos)]) {
		case AO:
			if(v==null){
			v = inflater.inflate(R.layout.fsm_listviewitem_action_analog, null);
			}
			 holder.sbValue = (SeekBar) v.findViewById(R.id.sbValue);
			 holder.tvValue = (TextView) v.findViewById(R.id.tvValue);
			 holder.btDelete = (Button) v.findViewById(R.id.btDelete);
			 holder.tvName = (TextView) v.findViewById(R.id.tvName);
			 holder.sbValue.setOnSeekBarChangeListener( holder.sbChangeListener);
			 holder.btDelete.setOnClickListener( holder.btDeleteClickListener);
			 holder.sbValue.setProgress(action.getValue());
			 holder.tvValue.setText("" + action.getValue());
			break;
		case DO:
			if(v==null){
			v = inflater
					.inflate(R.layout.fsm_listviewitem_action_digital, null);
			}
			 holder.tbValue = (ToggleButton) v.findViewById(R.id.tbValue);
			 holder.tvValue = (TextView) v.findViewById(R.id.tvValue);
			 holder.btDelete = (Button) v.findViewById(R.id.btDelete);
			 holder.tvName = (TextView) v.findViewById(R.id.tvName);
			 holder.btDelete.setOnClickListener( holder.btDeleteClickListener);
			 holder.tbValue
					.setOnCheckedChangeListener( holder.tbCheckedChangedListener);

			

			 holder.tbValue.setChecked( action.getValue() == 1 ? true
					: false);

			break;
		}
		
		 holder.tvName.setText(action.getAddressIdentifier().toString());
		 v.setTag(holder);
		return v;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	private class ViewHolder {
		public Action action;
		public TextView tvName, tvValue;
		public ToggleButton tbValue;
		public SeekBar sbValue;
		public Button btDelete;

		public OnSeekBarChangeListener sbChangeListener = new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvValue.setText("" + progress);
				action.setValue(progress);
			}
		};
		public OnClickListener btDeleteClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppData.currentWorkingTransition.removeAction(action);

			}
		};
		public OnCheckedChangeListener tbCheckedChangedListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					action.setValue(1);
				} else {
					action.setValue(0);
				}

			}
		};

	}

}
