package com.uday.dto;

public class SetAtmPin {
	
	private long atmNumber;
    private int pin;
    private int otp;

	public SetAtmPin() {
		super();
	}

	public SetAtmPin(long atmNumber, int pin, int otp) {
		super();
		this.atmNumber = atmNumber;
		this.pin = pin;
		this.otp = otp;
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

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "SetAtmPin [atmNumber=" + atmNumber + ", pin=" + pin + ", otp=" + otp + "]";
	}

	

	

	
	

}
