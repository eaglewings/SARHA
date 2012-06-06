package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import ch.fhnw.students.keller.benjamin.sarha.AppData;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class ExportConfigDialogFragment extends DialogFragment {
	static ExportConfigDialogFragment newInstance() {
	       ExportConfigDialogFragment f = new ExportConfigDialogFragment();
	       
	        
	        Bundle args = new Bundle();
	        //args.putInt("num", num);
	        args.putString("name", "newconfig");
	        f.setArguments(args);
	        
	        return f;
	    }
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 LinearLayout v = new LinearLayout(this.getActivity());
		 v.setOrientation(LinearLayout.VERTICAL);
		 final Spinner s = new Spinner(this.getActivity());
		 s.setAdapter(new ArrayAdapter<Config>(this.getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, AppData.configs));
		 v.addView(s);
		 final EditText e = new EditText(this.getActivity());
		 e.setText("");
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
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            ((ConfigManagerActivity)getActivity()).doExportConfigPositiveClick(e.getText().toString(),s.getSelectedItem());
	                        }
	                    }
	                )
	                .setNegativeButton("Cancel",
	                    new DialogInterface.OnClickListener() {
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
		//arg0.putString("name", input.getText().toString());
	}
	
}
