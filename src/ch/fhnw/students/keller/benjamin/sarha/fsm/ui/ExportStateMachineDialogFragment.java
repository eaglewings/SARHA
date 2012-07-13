package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;

public class ExportStateMachineDialogFragment extends DialogFragment {
	static ExportStateMachineDialogFragment newInstance() {
	       ExportStateMachineDialogFragment f = new ExportStateMachineDialogFragment();
	        Bundle args = new Bundle();
	        args.putString("name", "newconfig");
	        f.setArguments(args);
	         return f;
	    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 LinearLayout v = new LinearLayout(this.getActivity());
		 v.setOrientation(LinearLayout.VERTICAL);
		 final Spinner s = new Spinner(this.getActivity());
		 final EditText e = new EditText(this.getActivity());
		 ArrayAdapter<StateMachine> adapter = new ArrayAdapter<StateMachine>(this.getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, AppData.data.stateMachines);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 s.setAdapter(adapter);
		 s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override	
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(s.getItemAtPosition(position) !=null){
				e.setText(((StateMachine)s.getItemAtPosition(position)).getName().trim()+".stm");
				}
				else{
					e.setText("statemachine.stm");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		 v.addView(s);
		 e.setSingleLine();
		 v.addView(e);
		 if(savedInstanceState==null){
			 this.getArguments();
		 }
		 else{
		 }
		
	        return new AlertDialog.Builder(getActivity())
	                .setTitle("Export Config")
	                .setView(v)
	                .setPositiveButton("OK",
	                    new DialogInterface.OnClickListener() {
	                        @Override
							public void onClick(DialogInterface dialog, int whichButton) {
	                            ((StateMachineManagerActivity)getActivity()).doExportStateMachinePositiveClick(e.getText().toString(),s.getSelectedItem());
	                        }
	                    }
	                )
	                .setNegativeButton("Cancel",
	                    new DialogInterface.OnClickListener() {
	                        @Override
							public void onClick(DialogInterface dialog, int whichButton) {
	                            ExportStateMachineDialogFragment.this.dismiss();
	                        }
	                    }
	                )
	                .create();
	    }
	 @Override
	public void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
		//arg0.putString("name", input.getText().toString());
	}
	
}
