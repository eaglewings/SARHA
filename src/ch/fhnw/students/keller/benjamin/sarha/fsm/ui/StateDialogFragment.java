package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;

public class StateDialogFragment extends DialogFragment {
	private State state;
	private EditText etName;
	private Button btOk, btCancel;
	private CheckBox cbDelete;
	static StateDialogFragment newInstance(State state) {
	       StateDialogFragment f = new StateDialogFragment();
	       
	       f.state=state;
	        
	        return f;
	    }
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fsm_dialog_state, container);
		getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
		etName = (EditText) v.findViewById(R.id.etName);
		btOk = (Button) v.findViewById(R.id.btOk);
		cbDelete = (CheckBox) v.findViewById(R.id.cbDelete);
		btCancel = (Button) v.findViewById(R.id.btCancel);
		
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		
		
		if(state==null){
		getDialog().setTitle("New State");
		etName.setText("new state");
		getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_menu_add);
		cbDelete.setVisibility(View.GONE);
		btOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name= etName.getText().toString().trim();
				if(name.equals("")){
					Toast.makeText(StateDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
				}
				else{
					AppData.currentWorkingStateMachine.add(new State(AppData.currentWorkingStateMachine,name));
					StateDialogFragment.this.dismiss();
				}
				
				
			}
		});
		
		}
		else{
			getDialog().setTitle("Edit State");
			getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
					android.R.drawable.ic_dialog_info);
			etName.setText(state.getStateName());
			cbDelete.setVisibility(View.VISIBLE);
			
			btOk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String name= etName.getText().toString().trim();
					if(name.equals("")){
						Toast.makeText(StateDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
					}
					else{
						if(!state.getStateName().equals(name)){
							AppData.currentWorkingStateMachine.setChangeId();
						}
						state.setStateName(name);
						StateDialogFragment.this.dismiss();
					}
					if(cbDelete.isChecked()){
						AppData.currentWorkingStateMachine.remove(state);
						AppData.currentWorkingStateMachine.setChangeId();
						StateDialogFragment.this.dismiss();
					}
					
				}
			});
			
		}
		
		etName.selectAll();
		return v;
	}
}
