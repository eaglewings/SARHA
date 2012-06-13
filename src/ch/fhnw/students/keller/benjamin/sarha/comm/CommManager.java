package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Observer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

public class CommManager {
	protected static final int UDP_PORT = 55555;
	protected static final int DATAGRAM_SOCKET_TIMEOUT = 8000;
	protected static final int UDP_PACKET_LENGTH = 110;
	protected static final int DEVICE_NAME_STRING_START = 60;
	protected static final int DEVICE_NAME_STRING_LENGTH = 29;
	protected static final int DEVICE_ID_BYTES_START = 89;
	protected static final int DEVICE_ID_BYTES_LENGTH = 3;
	protected static final int PING_TIMEOUT = 250;
	protected static final String CON_OPEN = "*HELLO*";
	protected static final int CON_TIMEOUT = 500;
	protected static final int TCP_PORT = 2000;
	public ArrayList<Device> devices = new ArrayList<Device>();
	public Protocol protocol = new Protocol(new BufferedInputStream(System.in), new BufferedOutputStream(System.out));
	public DeviceModel device;
	private Device connectedDevice = null;
	private ArrayList<Observer> devicelistObservers = new ArrayList<Observer>();
	private ArrayList<Observer> dataReceiveObservers = new ArrayList<Observer>();
	private Thread broadcast = new Thread(),
			connection = new Thread();
	
	private Socket skt = null;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private boolean disconnect = false;

	private DeviceListPersistenceManager persistenceManager = null;
	private Context appcontext;

	public CommManager(DeviceListPersistenceManager persistenceManager,
			Context appcontext) {
		setDeviceListPersistenceManager(persistenceManager);
		this.appcontext = appcontext;
		restoreDeviceList();
	}

	public void connect(Device dev) {
		if (connectedDevice == null) { // No currently active connection
			if (devices.contains(dev)) { // Device is a valid device from the
											// list
				try {
					skt = new Socket();
					skt.connect(dev.getAddress());
					in = new BufferedInputStream(skt.getInputStream());
					out = new BufferedOutputStream(skt.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
					disconnect();
					return;
				}
				Thread test = new Thread() {
					public void run() {
						byte[] buffer = new byte[CON_OPEN.length()];
						try {
							in.read(buffer, 0, buffer.length);
							if (new String(buffer).equals(CON_OPEN) == false) {
								disconnect = true;
								return;
							}

						} catch (IOException e) {
							e.printStackTrace();
							disconnect = true;
							return;
						}
						System.out.println("testThread end");
					}
				};
				test.start();

				Thread wait = new Thread() {
					public void run() {
						try {
							sleep(CON_TIMEOUT);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				wait.start();
				while (wait.isAlive() && test.isAlive()) {
				}
				if (test.isAlive()) {
					disconnect();
					return;
				} else if (skt == null || disconnect == true) {
					disconnect();
					return;
				} else { // everything OK
					connectedDevice = dev;
					devices.get(devices.indexOf(dev)).setState(
							DeviceState.ONLINE);
					notifyDeviceListObservers();
					protocol = new Protocol(in, out);
					connection = new Thread() {
						public void run() {
							while (skt != null) {
								protocol.receive();
							}
						}

					};
					connection.start();
					try {
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		} else {
			if (dev.equals(connectedDevice) == false) {
				disconnect();
				connect(dev);
			}
		}
	}

	public void disconnect() {
		// TODO Verbindung abbrechen, Streams schliessen
		System.out.println("disconnect");
		try {
			if (skt != null) {
				skt.close();
				skt = null;
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
		}
		int index = devices.indexOf(connectedDevice);
		if (index != -1) {
			devices.get(index).setState(DeviceState.CONNECTABLE);
			notifyDeviceListObservers();
		}
		disconnect = false;
		connectedDevice = null;
		System.out.println("end");
	}


	public ArrayList<Device> getAvailableDevices() {
		return devices;
	}

	public void refreshList() {
		for (Device device : devices) {
			
				device.setState(DeviceState.UNKNOWN);
			
				System.out.println("Device: " + device);
		}
		notifyDeviceListObservers();
		// Check connection with connectedDevice
		if (connectedDevice != null) {
			System.out.println("connectedDev != null");
			if (protocol.getAck()) {
				devices.get(devices.indexOf(connectedDevice)).setState(
						DeviceState.ONLINE);// TODO auch nur
											// connectedDevice.setState()?
				notifyDeviceListObservers();
			} else {
				System.out.println("no ack");
				disconnect();
			}
		}
		if (broadcast.isAlive() == false) {
			broadcast = new BroadcastThread();
			broadcast.start();
		}

		saveDeviceList();

	}

	public void send() {
	}

	public void addDeviceListObserver(Observer obs) {
		devicelistObservers.add(obs);
	}

	public void removeDeviceListObserver(Observer obs) {
		devicelistObservers.remove(obs);
	}

	private void notifyDeviceListObservers() {
		for (Observer obs : devicelistObservers) {
			obs.update(null, devices);
		}
		saveDeviceList();
	};

	public void addDataReceiveObserver(Observer obs) {
		dataReceiveObservers.add(obs);
	}

	public void removeDataReceiveObserver(Observer obs) {
		dataReceiveObservers.remove(obs);
	}

	public void setDeviceListPersistenceManager(
			DeviceListPersistenceManager manager) {
		persistenceManager = manager;
	}

	public void saveDeviceList() {
		if (persistenceManager != null) {
			persistenceManager.saveDeviceList(devices);
		}
	}

	private void restoreDeviceList() {
		if (persistenceManager != null) {
			ArrayList<Device> restoredDevices = persistenceManager
					.restoreDeviceList();
			if (restoredDevices == null) {
				return;
			} else {
				devices = restoredDevices;
			}
		}
	}

	private class BroadcastThread extends Thread {
		private DatagramSocket skt = null;
		private ArrayList<Device> scannedDevices = new ArrayList<Device>();
		private MulticastLock ml = null;

		public void run() {
			System.out.println("broadcastthread started devices: " + devices.size());
			byte[] buf = new byte[2048];
			DatagramPacket pkt = new DatagramPacket(buf, buf.length);
			// Enable UDP boradcast by aquiring Multicast Lock; UDP
			// Broadcasts
			// are dumped by default for saving battery lifetime
			WifiManager wifi;
			wifi = (WifiManager) appcontext
					.getSystemService(Context.WIFI_SERVICE);
			ml = wifi.createMulticastLock("Receive UDP Broadcasts from WiFly");
			ml.setReferenceCounted(false);
			ml.acquire();

			try {
				skt = new DatagramSocket(UDP_PORT);
				skt.setSoTimeout(DATAGRAM_SOCKET_TIMEOUT);
			} catch (SocketException e) {
				e.printStackTrace();
				cleanup();
				System.out.println("broadcastthred stopped");
				return;

			}
			do {
				try {
					skt.receive(pkt);
				} catch (IOException e) {
					e.printStackTrace();
					cleanup();
					System.out.println("broadcastthread stopped");
					return;
				}
			} while (process(pkt));
			cleanup();
			
			System.out.println("boradcastthread stopped");
			// Broadcastempfang ausschalten
		}

		private boolean process(DatagramPacket pkt) {
			System.out.println("process pkt");
			byte[] data = pkt.getData();
			if (pkt.getLength() == UDP_PACKET_LENGTH) {
				System.out.println("length io");
				byte[] name = new byte[DEVICE_NAME_STRING_LENGTH];
				byte[] idbytes = new byte[DEVICE_ID_BYTES_LENGTH];
				for (int i = DEVICE_NAME_STRING_START; i < DEVICE_NAME_STRING_START
						+ DEVICE_NAME_STRING_LENGTH; i++) {
					name[i - DEVICE_NAME_STRING_START] = data[i];
					
					
				}
				for (int i = DEVICE_ID_BYTES_START; i < DEVICE_ID_BYTES_START
						+ DEVICE_ID_BYTES_LENGTH; i++) {
					idbytes[i - DEVICE_ID_BYTES_START] = data[i];
					System.out.println(
							Byte.toString(idbytes[i - DEVICE_ID_BYTES_START]));
				}

				Device device = new Device(new String(name).trim(), idbytes,
						new InetSocketAddress(pkt.getAddress(), TCP_PORT),
						DeviceState.CONNECTABLE);
				if (scannedDevices.contains(device)) {
					System.out.println("scanned devices contains device");
					return false;
				}
				scannedDevices.add(device);
				System.out.println("added to scanned devices");

				if (devices.contains(device)) {
					int index = devices.indexOf(device);
					if (devices.get(index).getState() != DeviceState.ONLINE) {
						devices.remove(device);
						devices.add(index, device);
					}

				} else {
					devices.add(device);
					System.out.println("device added to devices");

				}

				notifyDeviceListObservers();
				return true;
			}

			return true;
		}

		private void cleanup() {
			if (skt != null) {
				skt.close();
			}
			if (ml != null) {
				ml.release();
			}
			for (Device d : devices) {
				if (!scannedDevices.contains(d)) {
					d.setState(DeviceState.OFFLINE);
				}
			}
			notifyDeviceListObservers();
		}

	}

	/*
	 * private class PingThread extends Thread { public void run() {
	 * Log.d("Pingthread", "running"); // go through all devices in the list for
	 * (Device device : devices) { try { if (device.getAddress().getAddress()
	 * .isReachable(PING_TIMEOUT)) { // if the device is reachable, mark the
	 * device // as connectable. What happens if Port is // occupied?
	 * device.setState(DeviceState.CONNECTABLE); } else {
	 * device.setState(DeviceState.OFFLINE); } } catch (IOException e) {
	 * device.setState(DeviceState.OFFLINE); e.printStackTrace(); } // make
	 * changes known to observers notifyDeviceListObservers(); }
	 * Log.d("pingThread", "stopped"); } }
	 */

}
