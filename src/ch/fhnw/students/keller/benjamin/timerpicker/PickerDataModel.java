package ch.fhnw.students.keller.benjamin.timerpicker;

import java.util.Observable;

public class PickerDataModel extends Observable {
	private Picker picker, higherPicker;

	public PickerDataModel(Picker picker,Picker higherPicker, int min, int max, int inc, int div){
		addObserver(picker);
		this.picker = picker;
		this.min = min;
		this.max = max;
		this.inc = inc;
		this.div = div;
		this.higherPicker = higherPicker;

	}

	private int value, min, max, inc;
	private int div;

	public void valueUp() {
		System.out.println("valueUp() 1 " + value);
		value += inc;
		System.out.println("valueUp() 1 " + value);
		if (value > max) {
			value = min;
			if (!isHighestPicker()) {
				higherPicker.getModel().valueUp();
			}
		}
		setChanged();
		notifyObservers();
	}

	public void valueDown() {
		value -= inc;
		if (value < min) {
			value = max;
			value=(int) Math.floor(value/inc);
			value*=inc;
			if (!isHighestPicker()) {
				if (higherPicker.getModel().getValue() > 0) {
					higherPicker.getModel().valueDown();
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	public boolean isHighestPicker() {
		if (higherPicker == null) {
			return true;
		}
		return false;
	}

	public Picker getHighestPicker() {
		if (isHighestPicker()) {
			return this.picker;
		} else {
			return higherPicker.getModel().getHighestPicker();
		}
	}

	public void setValue(int input) {
		input/=inc;
		input*=inc;
		value = input % (max - min + 1);
		if (!isHighestPicker()) {
			System.out.println("SetValue " + input / (max - min + 1));
			higherPicker.getModel().addValue(input / (max - min + 1));
		}
		setChanged();
		notifyObservers();
	}

	public void addValue(int input) {
		value += input % (max - min + 1);
		System.out.println("addValue " +  input % (max - min + 1) +" input : " +input);
		if (value > max) {
			value = min + value - max;
			if (!isHighestPicker()) {
				higherPicker.getModel().valueUp();
			}
		}
		if (!isHighestPicker()) {
			higherPicker.getModel().addValue(input / (max - min + 1));
		}
		setChanged();
		notifyObservers();

	}

	public Picker getHigherPicker() {
		return higherPicker;
	}

	public void setHigherPicker(Picker higherPicker) {
		this.higherPicker = higherPicker;
		addObserver(higherPicker);
	}

	public int getValue() {
		return value;
	}

	public int getDiv() {
		return div;
	}
}
