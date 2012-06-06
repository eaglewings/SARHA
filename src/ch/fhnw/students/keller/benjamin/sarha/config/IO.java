package ch.fhnw.students.keller.benjamin.sarha.config;

import java.io.Serializable;
import java.util.EnumMap;

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
	static {
		typeHeaders.put(Type.DO, "Digital Outputs");
		typeHeaders.put(Type.DI, "Digital Inputs");
		typeHeaders.put(Type.AO, "Analog Outputs");
		typeHeaders.put(Type.AI, "Analog Inputs");
	}
	public static EnumMap<Type, String> typeDescriptors = new EnumMap<Type, String>(
			Type.class);
	static {
		typeDescriptors.put(Type.DO, "Digital Output");
		typeDescriptors.put(Type.DI, "Digital Input");
		typeDescriptors.put(Type.AO, "Analog Output");
		typeDescriptors.put(Type.AI, "Analog Input");
	}

	public static enum DigitalOut implements AddressIdentifier {

		DO0, DO1, DO2, DO3, DO4, DO5, DO6, DO7, DO8, DO9;
		
		@Override
		public Type getType() {
			return Type.DO;
		}

	}

	public enum DigitalIn implements AddressIdentifier {
		DI0, DI1, DI2, DI3, DI4, DI5, DI6, DI7, DI8, DI9;

		@Override
		public Type getType() {
			return Type.DI;
		}
		

	}

	public enum AnalogOut implements AddressIdentifier {
		AO0, AO1, AO2, AO3;

		@Override
		public Type getType() {
			return Type.AO;
		}

	}

	public enum AnalogIn implements AddressIdentifier {
		AI0, AI1, AI2, AI3;

		@Override
		public Type getType() {
			return Type.AI;
		}
	}

	public enum Digital {
		ON, OFF
	}
	
	public enum AnalogComperator {
		GREATER_THAN(">"), SMALLER_THAN("<");
		
		private final String symbol;
		AnalogComperator(String str){
			symbol=str;
		}
		public String getString() {
			return symbol;
		}
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