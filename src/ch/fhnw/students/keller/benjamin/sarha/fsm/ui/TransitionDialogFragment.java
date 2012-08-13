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
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

public class TransitionDialogFragment extends DialogFragment {
	private Transition transition;
	private EditText etName;
	private Button btOk, btCancel;
	private CheckBox cbDelete;
	static TransitionDialogFragment newInstance(Transition transition) {
	       TransitionDialogFragment f = new TransitionDialogFragment();
	       
	       f.transition=transition;
	        
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
		
		
		if(transition==null){
		getDialog().setTitle("New Transition");
		etName.setText("new transition");
		getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_menu_add);
		cbDelete.setVisibility(View.GONE);
		btOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name= etName.getText().toString().trim();
				if(name.equals("")){
					Toast.makeText(TransitionDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
				}
				else{
					AppData.currentWorkingState.add(new Transition(AppData.currentWorkingState, name));
					TransitionDialogFragment.this.dismiss();
				}
				
				
			}
		});
		
		}
		else{
			getDialog().setTitle("Edit Transition");
			getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
					android.R.drawable.ic_dialog_info);
			etName.setText(transition.getName());
			cbDelete.setVisibility(View.VISIBLE);
			
			btOk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String name= etName.getText().toString().trim();
					if(name.equals("")){
						Toast.makeText(TransitionDialogFragment.this.getActivity(), "Please provide a valid name", Toast.LENGTH_SHORT).show();
					}
					else{
						if(!transition.getName().equals(name)){
							AppData.currentWorkingStateMachine.setChangeId();
						}
						transition.setName(name);
						TransitionDialogFragment.this.dismiss();
					}
					if(cbDelete.isChecked()){
						AppData.currentWorkingState.remove(transition);
						AppData.currentWorkingStateMachine.setChangeId();
						TransitionDialogFragment.this.dismiss();
					}
					
				}
			});
			
		}
		
		etName.selectAll();
		return v;
	}
}
