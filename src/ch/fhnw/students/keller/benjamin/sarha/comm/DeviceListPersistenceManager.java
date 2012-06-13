package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.util.ArrayList;

public interface DeviceListPersistenceManager{
	void saveDeviceList(ArrayList<Device> devices);
	ArrayList<Device> restoreDeviceList();
}
