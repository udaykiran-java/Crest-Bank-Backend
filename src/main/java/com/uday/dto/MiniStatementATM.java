package com.uday.dto;

public class MiniStatementATM {

	private long atmNumber;
	private int pin;
	public MiniStatementATM() {
		super();
	}
	public MiniStatementATM(long atmNumber, int pin) {
		super();
		this.atmNumber = atmNumber;
		this.pin = pin;
	}
	public long getAtmNumber() {
		return atmNumber;
	}
	public void setAtmNumber(long atmNumber) {
		this.atmNumber = atmNumber;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	@Override
	public String toString() {
		return "MiniStatementATM [atmNumber=" + atmNumber + ", pin=" + pin + "]";
	}
	
	
	
	
	
}
