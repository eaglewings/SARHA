package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Action;
import ch.fhnw.students.keller.benjamin.sarha.fsm.DigitalOutAction;

public class OldActionAdapter extends ArrayAdapter<Action> {
	private Context context;
	private DigitalOutAction da;
	private int position;
	private View view;
	private View visibleDeleteButton;


	public OldActionAdapter(Context context, ArrayList<Action> actions) {
		
		super(context, R.layout.fsm_listviewitem_action_digital, actions);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		this.position=position;
		Action a = this.getItem(position);

		if (a instanceof DigitalOutAction) {
			da = (DigitalOutAction) a;
			if (v == null) {
						LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				v = inflater.inflate(R.layout.fsm_listviewitem_action_digital, parent, false);
				

			}

			if (a != null) {

				TextView txtView1 = (TextView) v.findViewById(R.id.textView1);
				ToggleButton toggleButton = (ToggleButton) v
						.findViewById(R.id.tbValue);
				Button deleteButton = (Button) v.findViewById(R.id.btDelete);
				deleteButton.setFocusable(true);
				deleteButton.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						Log.d("actionadapter", "focuschange"+v);
						if(!hasFocus){
							v.setVisibility(View.INVISIBLE);
							}
						
					}
				});
				deleteButton.setOnClickListener(new OnClickListener() {
					private int pos=OldActionAdapter.this.position;
					@Override
					public void onClick(View v) {
						v.setVisibility(View.INVISIBLE);
						remove(getItem(pos));
						
					}
				});
				
				toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					private DigitalOutAction da = OldActionAdapter.this.da;
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							da.setValue(1);
						}
						else{
							da.setValue(0);
						}
						
					}
				});

				txtView1.setText("Ausgangname");
				if (da.getValue()>0) {
					toggleButton.setChecked(true);
				} else {
					toggleButton.setChecked(false);
				}
				view=deleteButton;
				
				v.setOnTouchListener(new OnTouchListener() {
					private View view = OldActionAdapter.this.view;
					
					GestureDetector gd=new GestureDetector(new OnGestureListener() {
						
						@Override
						public boolean onSingleTapUp(MotionEvent e) {
							return false;
						}
						
						@Override
						public void onShowPress(MotionEvent e) {}
						
						@Override
						public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
								float distanceY) {
							return false;
						}
						
						@Override
						public void onLongPress(MotionEvent e) {}
						
						@Override
						public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
								float velocityY) {
							
							Animation animation = new AlphaAnimation(0.0f, 1.0f);
							animation.setDuration(300);
							view.startAnimation(animation);
							view.setVisibility(View.VISIBLE);
							visibleDeleteButton=view;
							view.requestFocus();
							return false;
						}
						
						@Override
						public boolean onDown(MotionEvent e) {
							if(visibleDeleteButton!=null){
								visibleDeleteButton.setVisibility(View.INVISIBLE);
								visibleDeleteButton=null;
							}
							return false;
						}
					});
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						gd.onTouchEvent(event);
						return true;
					}
				});

			}
		}
		return v;
	}
}
