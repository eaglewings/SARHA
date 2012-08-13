package ch.fhnw.students.keller.benjamin.sarha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import ch.fhnw.students.keller.benjamin.sarha.Importer.PortableType;

public class ExportDialogFragment extends DialogFragment {
	private PortableType type;
	public static ExportDialogFragment newInstance(PortableType type) {
	       ExportDialogFragment f = new ExportDialogFragment();
	       f.type = type;
	         return f;
	    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 LinearLayout v = new LinearLayout(this.getActivity());
		 v.setOrientation(LinearLayout.VERTICAL);
		 final Spinner s = new Spinner(this.getActivity());
		 final EditText e = new EditText(this.getActivity());
		 ArrayAdapter<Portable> adapter = new ArrayAdapter<Portable>(this.getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, type.list);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 s.setAdapter(adapter);
		 s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(s.getItemAtPosition(position) !=null){
				e.setText(((Portable)s.getItemAtPosition(position)).getName().trim()+".cfg");
				}
				else{
					e.setText("defaultName.cfg");
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
	                .setTitle("Export "+type.labelLC)
	                .setView(v)
	                .setPositiveButton("OK",
	                    new DialogInterface.OnClickListener() {
	                        @Override
							public void onClick(DialogInterface dialog, int whichButton) {
	                            Portable portable = (Portable) s.getSelectedItem();
	                            String string= e.getText().toString().trim();
	                            File root = Environment.getExternalStorageDirectory();
	                		    File folder = new File(root.getAbsolutePath()+"/"+AppData.APPLICATION_FOLDER+"/"+type.folder +"/");
	                		    folder.mkdirs();
	                		    File exportfile = new File(folder, string);
	                		    ObjectOutputStream outputStream = null;
	                		    if (root.canWrite()){
	                		    	try {
	                					outputStream = new ObjectOutputStream(new FileOutputStream(exportfile));
	                					outputStream.writeObject(portable);
	                		        } catch (FileNotFoundException e) {
	                		        	e.printStackTrace();
	                		        	Toast.makeText(getActivity(), "Sorry, FileNotFoundException occured.", Toast.LENGTH_LONG).show();
	                				} catch (IOException e) {
	                					e.printStackTrace();
	                					 Toast.makeText(getActivity(), "Sorry, an error occured.", Toast.LENGTH_LONG).show();
	                				}
	                            
	                		        
	                		    }
	                		    else{
	                		    	Toast.makeText(getActivity(), "Sorry, location "+root+" is write protected.", Toast.LENGTH_LONG).show();
	                		    }
	                            //Close the ObjectOutputStream
	                            try {
	                                if (outputStream != null) {
	                                    outputStream.flush();
	                                    outputStream.close();
	                                    Toast.makeText(getActivity(), "Config saved as: " + exportfile.getAbsolutePath(), Toast.LENGTH_LONG).show();
	                                }
	                            } catch (IOException ex) {
	                                ex.printStackTrace();
	                                Toast.makeText(getActivity(), "Sorry, an error occured.", Toast.LENGTH_LONG).show();
	                            }
	                        }
	                    }
	                )
	                .setNegativeButton("Cancel",
	                    new DialogInterface.OnClickListener() {
	                        @Override
							public void onClick(DialogInterface dialog, int whichButton) {
	                          
	                        }
	                    }
	                )
	                .create();
	    }
	
}
