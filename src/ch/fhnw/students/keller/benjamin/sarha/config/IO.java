package ch.fhnw.students.keller.benjamin.sarha.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

import ch.fhnw.students.keller.benjamin.sarha.AppData;

public class IO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -176688756205784271L;

	public static enum Type {
		DO, DI, AO, AI;
	};

	public static EnumMap<Type, String> typeHeaders = new EnumMap<Type, String>(
			Type.class);
	public static EnumMap<Type, String> typeDescriptors = new EnumMap<Type, String>(
			Type.class);
	static {
		typeHeaders.put(Type.DO, "Digital Outputs");
		typeHeaders.put(Type.DI, "Digital Inputs");
		typeHeaders.put(Type.AO, "Analog Outputs");
		typeHeaders.put(Type.AI, "Analog Inputs");
		typeDescriptors.put(Type.DO, "Digital Output");
		typeDescriptors.put(Type.DI, "Digital Input");
		typeDescriptors.put(Type.AO, "Analog Output");
		typeDescriptors.put(Type.AI, "Analog Input");
	}

	public static enum AddressIdentifierNotSet implements AddressIdentifier {
		AI_NOT_SET;

		@Override
		public Type getType() {
			return null;
		}

		@Override
		public int getOrdinal() {
			return ordinal();
		}

		@Override
		public AddressIdentifier[] getValues() {
			return AddressIdentifierNotSet.values();
		}
	}

	public static enum DigitalOut implements AddressIdentifier {

		DO0, DO1, DO2, DO3, DO4, DO5, DO6, DO7, DO8, DO9;

		@Override
		public Type getType() {
			return Type.DO;
		}

		@Override
		public int getOrdinal() {
			return ordinal();
		}

		@Override
		public AddressIdentifier[] getValues() {
			return DigitalOut.values();
		}

	}

	public enum DigitalIn implements AddressIdentifier {
		DI0, DI1, DI2, DI3, DI4, DI5, DI6, DI7, DI8, DI9;

		@Override
		public Type getType() {
			return Type.DI;
		}

		@Override
		public int getOrdinal() {
			return ordinal();
		}

		@Override
		public AddressIdentifier[] getValues() {
			return DigitalIn.values();
		}

	}

	public enum AnalogOut implements AddressIdentifier {
		AO0, AO1, AO2, AO3;

		@Override
		public Type getType() {
			return Type.AO;
		}

		@Override
		public int getOrdinal() {
			return ordinal();
		}

		@Override
		public AddressIdentifier[] getValues() {
			return AnalogOut.values();
		}

	}

	public enum AnalogIn implements AddressIdentifier {
		AI0, AI1, AI2, AI3;

		@Override
		public Type getType() {
			return Type.AI;
		}

		@Override
		public int getOrdinal() {
			return ordinal();
		}

		@Override
		public AddressIdentifier[] getValues() {
			return AnalogIn.values();
		}

	}

	public enum Digital {
		ON, OFF
	}

	public enum AnalogComperator {
		GREATER_THAN(">"), SMALLER_THAN("<");

		private final String symbol;

		AnalogComperator(String str) {
			symbol = str;
		}

		public String getString() {
			return symbol;
		}
	}
	
	public static String getName(AddressIdentifier ai) {
		Config config = AppData.currentWorkingStateMachine.getConfig();
		if(config==null){
			config=IO.defaultConfig();
		}
		config.getIOName(ai);
		return config.getIOName(ai);
	}

	public static ArrayList<AddressIdentifier> getAddressIdentifiersOfType(IO.Type type) {
		switch (type) {
		case DO:
			return new ArrayList<AddressIdentifier>(Arrays.asList(IO.DigitalOut.values()));
		case DI:
			return new ArrayList<AddressIdentifier>(Arrays.asList(IO.DigitalIn.values()));	
		case AO:
			return new ArrayList<AddressIdentifier>(Arrays.asList(IO.AnalogOut.values()));	
		case AI:
			return new ArrayList<AddressIdentifier>(Arrays.asList(IO.AnalogIn.values()));		
		default:
			return new ArrayList<AddressIdentifier>(Arrays.asList(IO.AddressIdentifierNotSet.values()));	
		}
		
		
		/*Attention! Following code caused rejection of the Dalvik VM!!! (7.6.2012)
		 * switch (type) {
		case DO:
			return DigitalOut.values();
		case DI:
			return DigitalIn.values();
		case AO:
			return AnalogOut.values();
		case AI:
			return AnalogIn.values();
		default:
			return AddressIdentifierNotSet.values();
		}*/
	}

	public static Config defaultConfig() {
		Config cfg = new Config("default");
		for (DigitalOut dio : DigitalOut.values()) {
			ch.fhnw.students.keller.benjamin.sarha.config.DigitalOut io = new ch.fhnw.students.keller.benjamin.sarha.config.DigitalOut();
			io.address = dio;
			io.name = dio.toString();
			cfg.add(io);
		}
		for (DigitalIn di : DigitalIn.values()) {
			ch.fhnw.students.keller.benjamin.sarha.config.DigitalIn io = new ch.fhnw.students.keller.benjamin.sarha.config.DigitalIn();
			io.address = di;
			io.name = di.toString();
			cfg.add(io);
		}
		for (AnalogOut ao : AnalogOut.values()) {
			ch.fhnw.students.keller.benjamin.sarha.config.AnalogOut io = new ch.fhnw.students.keller.benjamin.sarha.config.AnalogOut();
			io.address = ao;
			io.name = ao.toString();
			cfg.add(io);
		}
		for (AnalogIn ai : AnalogIn.values()) {
			ch.fhnw.students.keller.benjamin.sarha.config.AnalogIn io = new ch.fhnw.students.keller.benjamin.sarha.config.AnalogIn();
			io.address = ai;
			io.name = ai.toString();
			cfg.add(io);
		}

		return cfg;

	}
}