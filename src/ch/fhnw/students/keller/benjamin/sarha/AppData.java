package ch.fhnw.students.keller.benjamin.sarha;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import ch.fhnw.students.keller.benjamin.sarha.comm.Device;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceModel;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.fsm.State;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Transition;

/**
 * This singleton handles persistent data like statemachines, configs and
 * devices. Furthermore it provides references to currently edited objects of a
 * statemachine and a global reference to the UI handler and the application
 * context.
 * The instance of the singleton must be created at the beginning of onCreate() of the launcher Activity.
 * 
 * @author Benjamin
 * 
 */
public class AppData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4318650218914961084L;

	private static final String FILENAME_APPDATA = "SARHA_APPDATA";

	public static final String APPLICATION_FOLDER = "Sarha";

	public static final String CONFIGFOLDER = "Configs";

	public static final String STATEMACHINEFOLDER = "Statemachines";

	public static StateMachine currentWorkingStateMachine;
	public static State currentWorkingState;
	public static Transition currentWorkingTransition;

	public ArrayList<StateMachine> stateMachines = new ArrayList<StateMachine>();
	public ArrayList<Device> devices = new ArrayList<Device>();
	public ArrayList<Config> configs = new ArrayList<Config>();

	public static Context context;
	public static Handler handler;

	public static AppData data;

	public static DeviceModel currentWorkingDeviceModel;
	public static TextView debugTextView;
	

	private AppData(Context context) {
		AppData.context = context;
		System.out.println("data created ");
	}

	static public void createInstance(Context context) {
		System.out.println("create instance"+context.getFilesDir());
		if (data == null) {
			data = loadAppData(context);
			if (data == null) {
				data = new AppData(context);
			}
		}
		if(data.stateMachines==null || data.devices==null || data.configs==null){ //data is corrupt
			resetAppData(context);
		}
	}
	static public void resetAppData(Context context){
		data= new AppData(context.getApplicationContext());
	}

	private static AppData loadAppData(Context context) {
		AppData appData = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(FILENAME_APPDATA);
			ois = new ObjectInputStream(fis);
			appData = (AppData) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();

			}

		}
		return appData;

	}

	public static void saveAppData() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context
					.openFileOutput(FILENAME_APPDATA, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
