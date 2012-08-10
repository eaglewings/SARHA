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
	public static DeviceFinder deviceFinder = new DeviceFinder(AppData.context);
	private static Thread networkThread = new Thread(), connection;
	public static Protocol protocol = null;

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
	public static void connect(final Device device) {
		if (!networkThread.isAlive()) {
			networkThread = new Thread() {

				@Override
				public void run() {
					if (connectedDevice != null) {
						System.out.println("ConnectedDevice !=null " + connectedDevice);
						discon();
					}
					try {
						skt = new Socket();
						System.out.println("skt");
						skt.connect(device.getAddress());
						System.out.println("skt.connect");
						in = new BufferedInputStream(skt.getInputStream());
						out = new BufferedOutputStream(skt.getOutputStream());

						readHello();

						connectedDevice = device;
						connectedDevice.setState(DeviceState.ONLINE);
						protocol = new Protocol(in, out);
						connection = new Thread() {
							public void run() {
								while (skt != null && protocol!=null) {
									protocol.receive();
								}
							}

						};
						connection.start();
						notifyDeviceListObservers();

					} catch (IOException e) {
						e.printStackTrace();
						discon();
						return;
					}

				}
			};
			networkThread.start();
		}

	}

	/**
	 * Looks for the opening string sent by the WiFi module.
	 * 
	 * @throws IOException
	 *             if a timeout occurs or the received string doesn't match
	 */
	private static void readHello() throws IOException {
		System.out.println("hello");
		byte[] buffer = new byte[CON_OPEN_MSG.length()];
		skt.setSoTimeout(SOCKET_TIMEOUT);
		System.out.println("before read");
		in.read(buffer, 0, buffer.length);
		System.out.println("after read" +new String(buffer));
		if (!(new String(buffer).equals(CON_OPEN_MSG))) {
			throw new IOException();
		}
		skt.setSoTimeout(0);
	}
	
	private static void discon(){
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
		if (connectedDevice != null) {
			connectedDevice.setState(DeviceState.CONNECTABLE);
		}
		connectedDevice = null;
		protocol = null;
		connection= null;
		notifyDeviceListObservers();
	}

	/**
	 * Closes the socket and the I/O streams. Sets the connectedDevice to null
	 */
	public static void disconnect() {
		if (!networkThread.isAlive()) {
			networkThread = new Thread() {
			@Override
			public void run() {
				discon();
				super.run();
			}
		};
		networkThread.start();
		}

	}

	/**
	 * Calls update() for all registered observers interested in changes of the
	 * list of devices
	 */
	public static void notifyDeviceListObservers() {

		for (final Observer obs : deviceListObservers) {
			AppData.handler.post(new Runnable() {
				
				@Override
				public void run() {
					obs.update(null, null);
					
				}
			});
			
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
