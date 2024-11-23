package com.uday.dto;

public class TransferByATM {

	private long atmNumber;
	private int pin;
	private long toAccountNumber;
	private double amount;
	public TransferByATM() {
		super();
	}
	public TransferByATM(long atmNumber, int pin, long toAccountNumber, double amount) {
		super();
		this.atmNumber = atmNumber;
		this.pin = pin;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
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
	public long getToAccountNumber() {
		return toAccountNumber;
	}
	public void setToAccountNumber(long toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "TransferByATM [atmNumber=" + atmNumber + ", pin=" + pin + ", toAccountNumber=" + toAccountNumber
				+ ", amount=" + amount + "]";
	}
	
	
	
}
