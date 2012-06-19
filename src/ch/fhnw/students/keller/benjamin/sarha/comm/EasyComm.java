package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class EasyComm {
	private Socket skt;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private Thread connection;
	public Protocol protocol;
	public Protocol connect(SocketAddress adr){
		
		
		try {
			skt = new Socket();
			skt.connect(adr);
			in = new BufferedInputStream(skt.getInputStream());
			out = new BufferedOutputStream(skt.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			return null;

		}
		protocol = new Protocol(in, out);
		connection = new Thread() {
			public void run() {
				while (skt != null) {
					protocol.receive();
				}
			}

		};
		connection.start();
		return protocol;
	}
	
	public void disconnect(){
		
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
	}
	
	
	
}
