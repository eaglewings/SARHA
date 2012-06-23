package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observer;

import ch.fhnw.students.keller.benjamin.sarha.AppData;

/**
 * This class provides static functions concerning the communication with the
 * control unit.
 * 
 * @author Benjamin
 * 
 */
public class CommManager {
	/**
	 * Reference to the device that is currently connected. Null if no
	 * connection is established.
	 */
	public static Device connectedDevice;
	protected static final String CON_OPEN_MSG = "*HELLO*";
	protected static final int SOCKET_TIMEOUT = 1000;
	public static final int TCP_PORT = 2000;

	private static Socket skt;
	private static BufferedInputStream in;
	private static BufferedOutputStream out;
	private static DeviceFinder deviceFinder = new DeviceFinder(AppData.context);

	private static ArrayList<Observer> deviceListObservers = new ArrayList<Observer>();

	/**
	 * Tries to establish a connection to the provided device. On success the
	 * connectedDevice is set to the device passed as parameter. The state of
	 * the device is set to ONLINE and the observers of the list of devices are
	 * notified.
	 * 
	 * @param device
	 *            Device to be connected to.
	 */
	public static void connect(Device device) {
		if (connectedDevice != null) {
			disconnect();
		}
		try {
			skt = new Socket();
			skt.connect(device.getAddress());

			in = new BufferedInputStream(skt.getInputStream());
			out = new BufferedOutputStream(skt.getOutputStream());

			readHello();

			connectedDevice = device;
			connectedDevice.setState(DeviceState.ONLINE);
			notifyDeviceListObservers();

		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			return;
		}

	}

	/**
	 * Looks for the opening string sent by the WiFi module.
	 * 
	 * @throws IOException
	 *             if a timeout occurs or the received string doesn't match
	 */
	private static void readHello() throws IOException {
		byte[] buffer = new byte[CON_OPEN_MSG.length()];
		skt.setSoTimeout(SOCKET_TIMEOUT);
		in.read(buffer, 0, buffer.length);
		if (!buffer.toString().equals(CON_OPEN_MSG)) {
			throw new IOException();
		}
		skt.setSoTimeout(0);
	}

	/**
	 * Closes the socket and the I/O streams. Sets the connectedDevice to null
	 */
	public static void disconnect() {
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
		connectedDevice.setState(DeviceState.CONNECTABLE);
		connectedDevice = null;
	}

	/**
	 * Calls update() for all registered observers interested in changes of the
	 * list of devices
	 */
	public static void notifyDeviceListObservers() {

		for (Observer obs : deviceListObservers) {
			obs.update(null, null);
		}
	}

	/**
	 * Registers an observer for the list of devices
	 * 
	 * @param obs
	 *            Observer interested in changes of the list of devices
	 */
	public static void addDeviceListObserver(Observer obs) {
		deviceListObservers.add(obs);
	}

	/**
	 * Removes an observer for the list of devices
	 * 
	 * @param obs
	 */
	public static void removeDeviceListObserver(Observer obs) {
		deviceListObservers.remove(obs);
	}

	/**
	 * Creats a DeviceFinder thread which updates the list of devices.
	 */
	public static void findDevices() {
		if (!deviceFinder.isAlive()) {
			deviceFinder = new DeviceFinder(AppData.context);
		}

	}

}
