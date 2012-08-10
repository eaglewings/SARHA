package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import ch.fhnw.students.keller.benjamin.sarha.AppData;

public class DeviceFinder extends Thread {

	private DatagramSocket skt = null;
	private ArrayList<Device> scannedDevices = new ArrayList<Device>();
	private MulticastLock ml = null;
	private Context context;
	private ArrayList<Device> storedDevices = AppData.data.devices;
	
	private static final int UDP_PORT = 55555;
	private static final int DATAGRAM_SOCKET_TIMEOUT = 8000;
	private static final int UDP_PACKET_LENGTH = 110;
	private static final int DEVICE_NAME_STRING_START = 60;
	private static final int DEVICE_NAME_STRING_LENGTH = 26;
	private static final int DEVICE_ID_BYTES_START = 86;
	private static final int DEVICE_ID_BYTES_LENGTH = 6;
	
	public DeviceFinder(Context context){
		this.context=context;
		this.start();
	}
	
	public void run() {
		System.out.println("DeviceFinder started");
		byte[] buf = new byte[2048];
		DatagramPacket pkt = new DatagramPacket(buf, buf.length);
		
		for (Device d : storedDevices) {
				d.setState(DeviceState.UNKNOWN); //reset all stored devices
		}
		notifyDeviceListObservers();
		
		aquireMulticastLock();

		try {
			skt = new DatagramSocket(UDP_PORT); //open new DatagramSocket
			skt.setSoTimeout(DATAGRAM_SOCKET_TIMEOUT); //set timeout
		} catch (SocketException e) {
			e.printStackTrace();
			cleanup();
			return;
		}
		do {
			try {
				skt.receive(pkt);
			} catch (IOException e) {
				e.printStackTrace();
				cleanup();
				return;
			}
		} while (process(pkt)); //process packages as long as there is no exception
		cleanup();
		
		System.out.println("devicefinder finished without exception");
	}
/**
 * Enable UDP boradcast by aquiring Multicast Lock; UDP
 * Broadcasts are otherways dumped by default for saving battery lifetime
 */
	
	private void aquireMulticastLock() {
		WifiManager wifi;
		wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		ml = wifi.createMulticastLock("Receive UDP Broadcasts from SARHA control units");
		ml.setReferenceCounted(false);
		ml.acquire();	
	}

	private boolean process(DatagramPacket pkt) {
		byte[] data = pkt.getData();
		if (pkt.getLength() == UDP_PACKET_LENGTH) {
			System.out.println("length io");
			String name = extractDeviceName(data);
			String boardId = extractDeviceID(data);
			
			Device device = new Device(new String(name).trim(), boardId,
					new InetSocketAddress(pkt.getAddress(), CommManager.TCP_PORT),
					DeviceState.CONNECTABLE);

			
			if (scannedDevices.contains(device)) {
				System.out.println("scanned devices contains device");
				return false; // Device has been scanned already: stop searching
			}
			scannedDevices.add(device);
			System.out.println("added to scanned devices");

			if (storedDevices.contains(device)) {
				int index = storedDevices.indexOf(device);
				if (storedDevices.get(index).getState() != DeviceState.ONLINE) {
					storedDevices.remove(device);
					storedDevices.add(index, device);
				}

			} else {
				storedDevices.add(device);
				System.out.println("device added to devices");

			}

			notifyDeviceListObservers();
			return true;
		}

		return true;
	}
	private String extractDeviceName(byte[] data){
		byte[] name = Arrays.copyOfRange(data, DEVICE_NAME_STRING_START, DEVICE_NAME_STRING_START+DEVICE_NAME_STRING_LENGTH);
		return (new String(name)).trim();
	}
	private String extractDeviceID(byte[] data){
		byte[] boardId = Arrays.copyOfRange(data, DEVICE_ID_BYTES_START, DEVICE_ID_BYTES_START+DEVICE_ID_BYTES_LENGTH);
		return new String(boardId);
	}
	
	public void cleanup() {
		if (skt != null) {
			skt.close();
		}
		if (ml != null) {
			ml.release();
		}
		for (Device d : storedDevices) {
			if (!scannedDevices.contains(d)) {
				d.setState(DeviceState.OFFLINE);
			}
		}
		notifyDeviceListObservers();
	}
	
	private void notifyDeviceListObservers(){
		AppData.handler.post(new Runnable() {
			public void run() {
				CommManager.notifyDeviceListObservers();
			}
		});
		
	}

}
	
	

