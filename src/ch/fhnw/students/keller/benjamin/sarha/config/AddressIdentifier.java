package ch.fhnw.students.keller.benjamin.sarha.config;

public interface AddressIdentifier {
	IO.Type getType();
	int getOrdinal();
	AddressIdentifier[] getValues();
}
