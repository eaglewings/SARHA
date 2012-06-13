package ch.fhnw.students.keller.benjamin.sarha.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * Data model for configuration of input and output aliases 
 *
 */
/**
 * @author Benjamin
 *
 */
public class Config implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9184492273360860423L;
	public String name;
	public String deviceName;
	private int createId, changeId;
	public ArrayList<IO.Type> types = new ArrayList<IO.Type>();
	public EnumMap<IO.Type, ArrayList<IOs>> ios = new EnumMap<IO.Type, ArrayList<IOs>>(IO.Type.class);
	
	public Config(String name){
		for (IO.Type type : IO.Type.values()) {
			ios.put(type, new ArrayList<IOs>());
		}
		createId=(int) (System.currentTimeMillis()/1000);
		changeId=createId;
		this.name=name;
	}
	public void add(IOs io){
		if(!types.contains(io.type)){
			types.add(io.type);
		}
		ios.get(io.type).add(io);
		
		
		
	}
	public void remove(IOs io){
		ios.get(io.type).remove(io);
		if(ios.get(io.type).size()==0){
			types.remove(io.type);
		}
		
	}
	
	/**Counts configured elements of a specified IO-type 
	 * 
	 * @param type
	 * @return Number of elements with type type
	 */
	public int count(IO.Type type){
		return ios.get(type).size();
	}
	
	/**
	 * @return Number of all configured elements
	 */
	public int count(){
		int sum=0;
		for (IO.Type type : IO.Type.values()) {
			sum=sum+count(type);
		}
		return sum;
	}
	
	public int countH(IO.Type type){
		if(count(type)>0)
		return count(type)+1;
		else
			return 0;
	}
	public int countH(){
		int sum=0;
		for (IO.Type type : IO.Type.values()) {
			sum=sum+countH(type);
		}
		return sum;
	}
	
	/**
	 * @return ArrayList of all configured elements of AddressIdentifier
	 */
	public ArrayList<AddressIdentifier> getAssignedAddresses(){
		ArrayList<AddressIdentifier> l = new ArrayList<AddressIdentifier>();
			for (IO.Type type : types) {
				for(IOs io : ios.get(type)){
					l.add(io.address);
				}
			}
		return l;
	}
	
	public HashMap<AddressIdentifier, String> getIOMap(){
		HashMap<AddressIdentifier, String> map = new HashMap<AddressIdentifier, String>();
		for (IO.Type type : types) {
			for(IOs io : ios.get(type)){
				map.put(io.address, io.name);
			}
		}
		return map;
	}
	
	public String getIOName(AddressIdentifier identifier){
		String str=getIOMap().get(identifier);
		if(str!=null){
			return str;
		}
		else{
			return identifier.toString();
		}
	}
	public IOs getIO(AddressIdentifier ai){
		for (IOs io : ios.get(ai.getType())) {
			if (io.address==ai){
				return io;
			}
		}
		
			
		return null;
	}
	public ArrayList<IOs> getListOfType(IO.Type type){
		return ios.get(type);
		
	}
	
	public IOs getIO(int position){
		for (IO.Type type : IO.Type.values()){
			if(position==0){
				return null;
			}
			if(count(type) >=position){
				return ios.get(type).get(position-1);
			}
			else{
				position = position - countH(type);
			}
			
			
		}
		return null;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public int getCreateId(){
		return createId;
	}
	public void setChangeId(){
		changeId = (int) (System.currentTimeMillis()/1000);
	}
	public int getChangeId(){
		return changeId;
	}

}
