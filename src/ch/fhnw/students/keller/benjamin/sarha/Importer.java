package ch.fhnw.students.keller.benjamin.sarha;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import ch.fhnw.students.keller.benjamin.sarha.comm.CommManager;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.ui.ConfigManagerAdapter;
import ch.fhnw.students.keller.benjamin.sarha.fsm.StateMachine;
import ch.fhnw.students.keller.benjamin.sarha.fsm.ui.StateMachineManagerAdapter;

public class Importer {
	public static enum CompareResult {
		MATCH, OVERWRITE, RENAME, NO_MATCH, NONE
	};
	public static enum PortableType{
		
		CONFIG("Config", AppData.CONFIGFOLDER, AppData.data.configs), STATEMACHINE("Statemachine", AppData.STATEMACHINEFOLDER, AppData.data.stateMachines);
		public String labelLC,labelUC, folder;
		public ArrayList<Portable> list;
		PortableType(String labelUC,String folder, ArrayList<Portable> list){
			this.labelUC=labelUC;
			this.labelLC=labelUC.toLowerCase();
			this.folder=folder;
			this.list= list;

			
		}
		public BaseAdapter getListAdapter(Context context){
			switch (this) {
			case CONFIG:
				return new ConfigManagerAdapter(context, AppData.data.configs);
			case STATEMACHINE:
				return new StateMachineManagerAdapter(context, AppData.data.stateMachines);
			default:
				return null;
			}
		}
	};

	ArrayList<boolean[]> comp;
	public boolean importImportable(PortableType type,Portable imported, boolean overWrite) {
		boolean success = false;
		int index;
		ArrayList<CompareResult> result = compare(imported);
		Portable matchedPortable = getMatchedPortable(type, result, getMatchType(result));
		switch (getMatchType(result)) {
		case OVERWRITE:
			if (overWrite) {
				if (type.list.contains(matchedPortable)) {
					index = type.list.indexOf(matchedPortable);
					type.list.remove(matchedPortable);
					type.list.add(index, imported);
					success = true;
				}
			}
			break;
		case NO_MATCH:
			type.list.add(imported);
			success = true;
			break;
		default:
			break;
		}
		return success;
	}

	private static ArrayList<CompareResult> compare(PortableType type, String name, int createId,
			int changeId) {
		ArrayList<CompareResult> result = new ArrayList<Importer.CompareResult>();
		for (Portable portable : type.list) {
			if ((portable.getCreateId() == createId) && (portable.getChangeId() == changeId)) {
				result.add(CompareResult.MATCH);
			} else if ((portable.getCreateId() == createId)) {
				result.add(CompareResult.OVERWRITE);
			} else if (portable.getName().equals(name)) {
				result.add(CompareResult.RENAME);
			} else {
				result.add(CompareResult.NO_MATCH);
			}
		}
		if (type.list.size() == 0) {
			result.add(CompareResult.NO_MATCH);
		}
		if (name.equals("0") && createId == 0 && changeId == 0) {
			result = new ArrayList<Importer.CompareResult>();
			result.add(CompareResult.NONE);
		}

		return result;

	}
	private static ArrayList<CompareResult> compare(Portable imported){
		return compare(imported.getPortableType(), imported.getName(), imported.getCreateId(), imported.getChangeId());
	}

	public static CompareResult getMatchType(PortableType type, String name, int createId,
			int changeId) {
		return getMatchType(compare(type, name, createId, changeId));
	}

	private static CompareResult getMatchType(ArrayList<CompareResult> matchList) {
		if (matchList.indexOf(CompareResult.NONE) > -1) {
			return CompareResult.NONE;
		} else if (matchList.indexOf(CompareResult.MATCH) > -1) {

			return CompareResult.MATCH;
		} else if (matchList.indexOf(CompareResult.OVERWRITE) > -1) {
			return CompareResult.OVERWRITE;
		} else if (matchList.indexOf(CompareResult.RENAME) > -1) {
			return CompareResult.RENAME;
		} else {
			return CompareResult.NO_MATCH;
		}
	}

	public static Portable getMatchedPortable(PortableType type,String name, int createId,
			int changeId) {
		return getMatchedPortable(type, compare(type, name, createId, changeId),
				getMatchType(type, name, createId, changeId));
	}

	private static Portable getMatchedPortable(PortableType type, ArrayList<CompareResult> matchList,
			CompareResult matchType) {
		if (matchType != CompareResult.NO_MATCH) {
			return type.list.get(matchList.indexOf(matchType));
		}
		return null;
	}

	public static Portable getFromFile(Context context, File file) {
		Portable portable = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			portable = (Portable) ois.readObject();
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
		return portable;

	}
	public static Portable getFromDevice(PortableType type, ArrayBlockingQueue<Integer> queue){
		switch (type) {
		case CONFIG:
			return CommManager.protocol.getPortable(type, queue);
		case STATEMACHINE:
			return CommManager.protocol.getPortable(type, queue);	

		default:
			return null;
		}
	}
	public static boolean sendToDevice(Portable portable, ArrayBlockingQueue<Integer> queue){
		switch (portable.getPortableType()) {
		case CONFIG:
			return CommManager.protocol.setConfig((Config) portable, queue);
		case STATEMACHINE:
			return CommManager.protocol.setStateMachine((StateMachine) portable, queue);

		default:
			return false;
		}
	}

	

}
