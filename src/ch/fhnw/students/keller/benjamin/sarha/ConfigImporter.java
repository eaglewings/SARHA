package ch.fhnw.students.keller.benjamin.sarha;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.content.Context;

import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class ConfigImporter {
	public static enum CompareResult {
		MATCH, OVERWRITE, RENAME, NO_MATCH, NONE
	};

	ArrayList<boolean[]> comp;

	public boolean importConfig(Config impConfig, boolean overWrite) {
		boolean success = false;
		int index;
		ArrayList<CompareResult> result = compare(impConfig.name,
				impConfig.createId, impConfig.getChangeId());
		Config matchedConfig = getMatchedConfig(result, getMatchType(result));
		switch (getMatchType(result)) {
		case OVERWRITE:
			if (overWrite) {
				if (AppData.data.configs.contains(matchedConfig)) {
					index = AppData.data.configs.indexOf(matchedConfig);
					AppData.data.configs.remove(matchedConfig);
					AppData.data.configs.add(index, impConfig);
					success = true;
				}
			}
			break;
		case NO_MATCH:
			AppData.data.configs.add(impConfig);
			success = true;
			break;
		default:
			break;
		}
		return success;
	}

	private static ArrayList<CompareResult> compare(String name, int createId,
			int changeId) {
		ArrayList<CompareResult> result = new ArrayList<ConfigImporter.CompareResult>();
		for (Config cfg : AppData.data.configs) {
			if ((cfg.createId == createId) && (cfg.getChangeId() == changeId)) {
				result.add(CompareResult.MATCH);
			} else if ((cfg.createId == createId)) {
				result.add(CompareResult.OVERWRITE);
			} else if (cfg.name.equals(name)) {
				result.add(CompareResult.RENAME);
			} else {
				result.add(CompareResult.NO_MATCH);
			}
		}
		if (AppData.data.configs.size() == 0) {
			result.add(CompareResult.NO_MATCH);
		}
		if (name.equals("0") && createId == 0 && changeId == 0) {
			result = new ArrayList<ConfigImporter.CompareResult>();
			result.add(CompareResult.NONE);
		}

		return result;

	}

	public static CompareResult getMatchType(String name, int createId,
			int changeId) {
		return getMatchType(compare(name, createId, changeId));
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

	public static Config getMatchedConfig(String name, int createId,
			int changeId) {
		return getMatchedConfig(compare(name, createId, changeId),
				getMatchType(name, createId, changeId));
	}

	private static Config getMatchedConfig(ArrayList<CompareResult> matchList,
			CompareResult matchType) {
		if (matchType != CompareResult.NO_MATCH) {
			return AppData.data.configs.get(matchList.indexOf(matchType));
		}
		return null;
	}

	public static Config getFromFile(Context context, File file) {
		Config config = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			config = (Config) ois.readObject();
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
		return config;

	}

}
