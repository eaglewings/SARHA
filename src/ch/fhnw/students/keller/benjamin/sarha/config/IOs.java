package ch.fhnw.students.keller.benjamin.sarha.config;

import java.io.Serializable;
import java.util.Observable;

import ch.fhnw.students.keller.benjamin.sarha.config.IO.AddressIdentifierNotSet;

public abstract class IOs extends Observable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7766739616947547730L;
	public String name="";
	public IO.Type type;
	public AddressIdentifier address=AddressIdentifierNotSet.AI_NOT_SET;
	private int value = 0, overrideValue = 0;
	private boolean overridden = false;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {

		this.value = value;
		setChanged();
	}

	public int getOverrideValue() {
		return overrideValue;
	}

	public void setOverrideValue(int value) {
		this.overrideValue = value;
		setChanged();
	}

	public void override() {
		overridden = true;
		setChanged();
	}

	public void real() {
		
		overridden = false;
		setChanged();
	}

	public boolean isOverridden() {
		return overridden;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IOs) {
			IOs io = (IOs) o;
			if (io.address.equals(address) && io.name.equals(name)
					&& io.type.equals(type)) {
				return true;
			}
		}

		return false;

	}
}
