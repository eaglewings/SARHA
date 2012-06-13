package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.fhnw.students.keller.benjamin.sarha.config.AddressIdentifier;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class Protocol {
	private static enum C {
		GET("get"), SET("set"), RET("ret"), ACK("ack"), NAK("nak");
		private final String rep;

		C(String str) {
			rep = str;
		}

		public String toString() {
			return rep;
		}
	};

	private enum ReceiveMode {
		COMMAND_MODE, DATA_MODE;
	}

	ReceiveMode mode = ReceiveMode.COMMAND_MODE;
	//@formatter:off
	private static final String COMMAND_SEPARATOR = ";", S = COMMAND_SEPARATOR;
	private static final String 
								GET="get",
								SET="set",
								RET="ret",
								ACK="ack",
								NAK="nak",
								CONFIG = "config", 
								NEW = "new", 
								ID = "id",
								FILE = "file", 
								CONFIGCFG = "config.cfg", 
								UPDATE = "update",
								OFF = "off", 
								ON = "on", 
								REMOTE = "remote", 
								IO = "io";
	//@formatter:on
	protected static final long ACK_TIMEOUT = 2000;
	private static final int COMMAND_LENGTH = 3;
	private static final int MAX_COMMAND_LENGTH = 500;
	private static final int BLOCKSIZE = 64;
	private static final int DATA_OFFER_TIMEOUT = 100;
	private static final int DATA_WAIT_TIMEOUT = 5000;

	private BufferedInputStream in;
	private BufferedOutputStream out;
	private ArrayBlockingQueue<C> commandQueue = new ArrayBlockingQueue<Protocol.C>(
			1);
	private ArrayBlockingQueue<byte[]> dataQueue = new ArrayBlockingQueue<byte[]>(
			1);
	byte[] commandBuffer = new byte[MAX_COMMAND_LENGTH];
	byte[] dataBuffer = new byte[BLOCKSIZE + 1];
	private DeviceModel deviceModel;

	public Protocol(BufferedInputStream in, BufferedOutputStream out) {
		this.in = in;
		this.out = out;
		try {
			this.out.write("hello world from Protocol".getBytes());
			this.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getAddressValue(IOs io) {
		sendCommand(GET, IO, io.address.toString());

		return 0;
	}

	public void setAddressValue(IOs io, int value) {
		sendCommand(SET, IO, io.address.toString(), Integer.toString(value));
	}

	public boolean setConfig(Config cfg) {
		String command = C.SET + S + FILE + S + CONFIGCFG + S;

		if (sendObject(command, cfg) == false) {
			return false;
		}
		if (!sendCommand(C.SET + S + CONFIG + S + ID + S + cfg.name + S
				+ cfg.getCreateId() + S + cfg.getChangeId()))
			;

		return waitAck();

	}

	public Config getConfig() {
		byte[] databuffer = new byte[BLOCKSIZE + 1];

		if (sendCommand(C.GET + S + FILE + S + CONFIGCFG)) {
			try {
				if (commandQueue.poll(ACK_TIMEOUT, TimeUnit.MILLISECONDS) == C.RET) {
					byte[] data = dataQueue.poll(DATA_OFFER_TIMEOUT,
							TimeUnit.MILLISECONDS);
					if (data != null) {
						String strData = new String(data);
						Pattern p = Pattern.compile("\\d+");
						Matcher m = p.matcher(strData);
						if (m.find()) {
							strData = m.group();
						} else {
							strData = null;
						}
						System.out.println("strdata.length" + strData.length()
								+ "?Ret data :" + strData);
						int filesize = Integer.parseInt(strData);
						mode = ReceiveMode.DATA_MODE;

						int blocks = (int) Math.ceil((float) filesize
								/ BLOCKSIZE);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						sendCommand("" + C.ACK);
						for (int i = 0; i < blocks; i++) {
							// System.out.println("for block " + i +
							// "of "+blocks+" blocks");
							int checksum = 0;
							databuffer = dataQueue.poll(DATA_WAIT_TIMEOUT,
									TimeUnit.MILLISECONDS);
							if (databuffer != null) {
								for (int j = 0; j < BLOCKSIZE; j++) {
									checksum += (0xFF & (short) databuffer[j]);

								}
								// System.out.println("checksum:" +
								// checksum+"(byte) checksum & 0xFF:"+(byte)(checksum&0xFF)
								// + " databuffer[BLOCKSIZE]:"
								// + databuffer[BLOCKSIZE]);
								if ((byte) (checksum & 0xFF) == databuffer[BLOCKSIZE]) {

									if (i == blocks - 1
											&& filesize % BLOCKSIZE != 0) {// The
																			// last
										// block is
										// padded
										// with
										// zeros
										baos.write(Arrays.copyOfRange(
												databuffer, 0, filesize
														% BLOCKSIZE));
									} else {
										baos.write(Arrays.copyOfRange(
												databuffer, 0, BLOCKSIZE));
									}
									sendCommand("" + C.ACK);
									continue;

								} else {
									i--;
									continue;
								}

							} else {
								// System.out.println("databuffer==null!!!!");
								mode = ReceiveMode.COMMAND_MODE;
								return null;
							}
						} // End iteration over blocks

						ObjectInputStream ois = new ObjectInputStream(
								new ByteArrayInputStream(baos.toByteArray()));
						return (Config) ois.readObject();

					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// System.out.println("finally");
				dataQueue.clear();
				mode = ReceiveMode.COMMAND_MODE;
			}
		}
		return null;

	}

	public boolean getAck() {
		System.out.println("Protocol.getAck enter");
		if (!sendCommand(GET, ACK)) {
			System.out.println("Protocol.getAck sendcommand failed");
			return false;
		}
		if (waitAck()) {
			return true;
		} else {
			return false;
		}
	}

	public void updateIOs(ArrayList<AddressIdentifier> addresses) {
		String str = "";

		for (int i = 0; i < addresses.size(); i++) {
			str += addresses.get(i).toString() + S;
		}
		str += addresses.get(addresses.size() - 1);
		sendCommand(SET, UPDATE, str);
	}

	public void setRemote(boolean onOff) {
		if (onOff) {
			sendCommand(SET, REMOTE, ON);
		} else {
			sendCommand(SET, REMOTE, OFF);
		}
	}

	private boolean send(byte[] buffer) {
		System.out.println("Protocol.send length: " + buffer.length);
		try {
			out.write(buffer);
			out.flush();
			return true;
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

	}

	private boolean sendCommand(String... args) {
		String command = "";
		for (int i = 0; i < args.length - 1; i++) {
			command += args + S;
		}
		command += args[args.length - 1] + "\r\n";

		System.out.println("Protocol.sendCommand: " + command);
		return send(command.getBytes());
	}

	public boolean setAddress() {
		return true;
	}

	private boolean sendObject(String command, Serializable obj) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ObjectOutputStream oos;
		int size, blocks, nackcount = 0;

		byte[] send = new byte[(BLOCKSIZE + 1)];
		System.out.println("send.length" + send.length);
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		size = baos.size();
		if (sendCommand(command + Integer.toString(size))) {
			if (waitAck()) {

				blocks = (int) Math.ceil((float) size / BLOCKSIZE);
				System.out.println("BLOCKSIZE: " + BLOCKSIZE + " blocks: "
						+ blocks);
				byte[] bytes = baos.toByteArray();
				System.out.println("bytes.length: " + bytes.length);
				for (int i = 0; i < blocks; i++) {
					int checksum = 0;
					System.out.println("i: " + i + " i * BLOCKSIZE=" + i
							* BLOCKSIZE + " (i+1)*BLOCKSIZE=" + (i + 1)
							* BLOCKSIZE);
					send = Arrays.copyOf(
							Arrays.copyOfRange(bytes, i * BLOCKSIZE, (i + 1)
									* BLOCKSIZE), BLOCKSIZE + 1);
					for (int j = 0; j < BLOCKSIZE; j++) {
						checksum += (0xFF & (short) send[j]);
					}
					System.out.println("checksum=" + checksum
							+ " checksum &0xFF: " + (checksum & 0xFF));

					send[BLOCKSIZE] = (byte) (checksum & 0xFF);
					System.out.println("send[BLOCKSIZE]" + send[BLOCKSIZE]);
					if (send(send)) {
						if (waitAck()) {
							nackcount = 0;
							continue;
						} else if (nackcount < 1) {
							i--;
							nackcount++;
							continue;
						} else {
							return false;
						}
					} else {
						return false;
					}

				}
				return true;
			}

		}
		return false;
	}

	private boolean waitAck() {
		System.out.println("Protocol.waitAck enter");
		try {
			if (commandQueue.poll(ACK_TIMEOUT, TimeUnit.MILLISECONDS) == C.ACK) {
				System.out.println("Protocol.waitAck return true");
				return true;
			} else {
				System.out.println("Protocol.waitAck return false");
				return false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void receive() {
		switch (mode) {
		case COMMAND_MODE:
			commandFilter();
			break;
		case DATA_MODE:

			try {
				if (in.available() >= BLOCKSIZE + 1) {
					System.out.println("BLOCKSIZE + 1 available");
					if (in.read(dataBuffer) == BLOCKSIZE + 1) {
						System.out.println("read BLOCKSIZE + 1 bytes");
						if (dataQueue.offer(dataBuffer, DATA_OFFER_TIMEOUT,
								TimeUnit.MILLISECONDS)) {
							System.out.println("offered successfully to queue:"
									+ new String(dataBuffer));
							break;// Everything allright.
						}
						dataQueue.clear(); // clean Queue
					}
					mode = ReceiveMode.COMMAND_MODE; // If there is an error
														// while reading data
														// switch back to
														// CommandMode
				}
			} catch (IOException e) {
				mode = ReceiveMode.COMMAND_MODE;
				e.printStackTrace();
			} catch (InterruptedException e) {
				mode = ReceiveMode.COMMAND_MODE;
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Recognize commands from the InputStream
	 * 
	 * 
	 */
	private void commandFilter() {
		C command = null;
		try {
			if (in.available() >= COMMAND_LENGTH) {
				System.out.println("Protocol.commandfilter available bytes: "
						+ in.available());
				in.read(commandBuffer, 0, COMMAND_LENGTH);
				for (C c : C.values()) {
					if ((new String(Arrays.copyOf(commandBuffer, COMMAND_LENGTH))).equals(c.toString())) {
						command = c;
						System.out
								.println("Protocol.commandfilter Command recognized: "
										+ command);
						break;
					}
				}
				if (command != null) {
					char eol;
					int i = COMMAND_LENGTH;
					do {
						if (i < MAX_COMMAND_LENGTH) {
							while ((in.read(commandBuffer, i, 1) != 1)) {
							}
							eol = (char) commandBuffer[i];
							i++;
						} else {
							return;
						}
					} while (eol != '\n');
					System.out.println("Protocol.commandfilter after do");
					parseCommand(command, commandBuffer);
				} else {
					// Data received but is no command... TODO how to dump??
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseCommand(C command, byte[] commandBuffer) {
		System.out.println("Protocol.parseCommand " + command
				+ "commandBuffer:" + new String(commandBuffer));
		switch (command) {
		case ACK:
			try {
				if (commandQueue.offer(command, ACK_TIMEOUT,
						TimeUnit.MILLISECONDS) == false) {
					commandQueue.clear();
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			break;
		case NAK:
			try {
				if (commandQueue.offer(command, ACK_TIMEOUT,
						TimeUnit.MILLISECONDS) == false) {
					commandQueue.clear();
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			break;
		case GET:

			break;
		case RET:
			String[] parts = new String(commandBuffer).split(S);
			if (parts[1].equals(UPDATE)) {
				ArrayList<AddressIdentifier> adresses = deviceModel.getConfig()
						.getAssignedAddresses();
				for (int i = 0; i < adresses.size(); i++) {
					deviceModel.setIOvalue(adresses.get(i),
							Integer.parseInt(parts[i]));

				}

			} else {
				try {
					if (!commandQueue.offer(command, ACK_TIMEOUT,
							TimeUnit.MILLISECONDS)) {
						commandQueue.clear();
						return;
					}
					if (!dataQueue.offer(commandBuffer, DATA_OFFER_TIMEOUT,
							TimeUnit.MILLISECONDS)) {
						dataQueue.clear();
						return;
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
			break;
		case SET:

			break;

		default:
			break;
		}

	}

}
