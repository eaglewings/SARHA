package ch.fhnw.students.keller.benjamin.sarha;

import java.util.ArrayList;
import java.util.Date;

import ch.fhnw.students.keller.benjamin.sarha.comm.Device;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;

public class Utils {

	
	
	public static boolean checkConfigName(String str){
		if(str== null || str.equals("") || str.equals("0")){
			return false;
		}
		for (Config conf : AppData.data.configs) {
			if(conf.name.equals(str)){
				return false;
			}
		}
		
		
		return true;
		
	}

	public static ArrayList<Device> getConnectableDevices() {
		ArrayList<Device> connectableDevices= new ArrayList<Device>();
		for (Device device : AppData.data.devices) {
			if(device.isConnectable() || device.isOnline()){
				connectableDevices.add(device);
			}
		}
		return connectableDevices;
	}
	
	public static String idString(int createId,int changeId){
		return "created: "+(new Date((long)createId*1000)).toLocaleString()+" changed: "+(new Date((long)changeId*1000)).toLocaleString();
	}
}
