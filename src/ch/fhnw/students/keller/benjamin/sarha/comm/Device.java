package ch.fhnw.students.keller.benjamin.sarha.comm;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;
/**
 * Repräsentation einer Steuereinheit
 * @author Benjamin Keller
 *
 */
public class Device implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String boardId;
	private InetSocketAddress address;
	private DeviceState state=DeviceState.UNKNOWN;
	/**
	 * 
	 * @param name Name der Steuereinheit
	 */
	private Device(String name){
		this.name=name;
	}
	/**
	 * 
	 * @param name Name der Steuereinheit
	 * @param address Socketadresse der Steuereinheit
	 */
	private Device(String name, InetSocketAddress address){
		this(name);
		this.address=address;
	}
	public Device(String name, String boardId, InetSocketAddress address){
		this(name, address);
		this.boardId=boardId;
	}
	
	public Device(String name, String boardId, InetSocketAddress address, DeviceState state){
		this(name,boardId,address);
		this.state=state;
	}
	
	/**
	 * Setzt Socketadresse der Steuereinheit
	 * @param address Socketadresse der Steuereinheit
	 */
	public void setAdress(InetSocketAddress address){
		this.address = address;
	}
	/**
	 * Setzt Namen der Steuereinheit
	 * @param name Name der Steuereinheit
	 */
	public void setName(String name){
		this.name = name;
	}
	
/**
 * Gibt Socketadresse der Steuereinheit zurück
 * @return Socketadresse der Steuereinheit
 */
	public InetSocketAddress getAddress(){
		return address;
	}
	/**
	 * Gibt Namen der Steuereinheit zurück
	 * @return Namen der Steuereinheit
	 */
	public String getName(){
		return name;
	}
	
	
	public String toString(){
		return name;
	}
	public String getBoardId() {
		
		return boardId;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof Device){
			Device dev= (Device) obj;
		return boardId.equals(dev.getBoardId());
	}
		return false;
	}
	
	
	public void setState(DeviceState state){
		this.state=state;
	}
	
	public DeviceState getState(){
		return state;
	}
	public boolean isOnline() {
		if(state==DeviceState.ONLINE){
		return true; }
		else {
			return false;
		}
	}
	public boolean isConnectable(){
		if(state==DeviceState.CONNECTABLE){
			return true; }
			else {
				return false;
			}
	}
	
}
