package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class NewConfigDialogFragment extends DialogFragment {
	private Bundle workingBundle;
	private EditText input;
	static NewConfigDialogFragment newInstance() {
       NewConfigDialogFragment f = new NewConfigDialogFragment();
       
        
        Bundle args = new Bundle();
        //args.putInt("num", num);
        args.putString("name", "newconfig");
        f.setArguments(args);
        
        return f;
    }
	
	 @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 input = new EditText(this.getActivity());
		 input.setSingleLine();
		 if(savedInstanceState==null){
			 workingBundle=this.getArguments();
		 }
		 else{
			 workingBundle=savedInstanceState;
		 }
		
	        input.setText(workingBundle.getString("name"));
	        return new AlertDialog.Builder(getActivity())
	                .setTitle("New Config Name")
	                .setView(input)
	                .setPositiveButton("OK",
	                    new DialogInterface.OnClickListener() {
	                        @Override
							public void onClick(DialogInterface dialog, int whichButton) {
	                            ((ConfigManagerActivity)getActivity()).doNewConfigPositiveClick(input.getText().toString());
	                        }
	                    }
	                )
	                .setNegativeButton("Cancel",
	                    new DialogInterface.OnClickListener() {
	                        @Override
							public void onClick(DialogInterface dialog, int whichButton) {
	                            ((ConfigManagerActivity)getActivity()).doNewConfigNegativeClick();
	                        }
	                    }
	                )
	                .create();
	    }
	 @Override
	public void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
		arg0.putString("name", input.getText().toString());
	}
}
