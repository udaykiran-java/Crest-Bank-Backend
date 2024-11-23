package com.uday.dto;

public class AtmTransactions {

	private long atmNumber;
	private int pin;
	private double amount;
	public AtmTransactions() {
		super();
	}
	public AtmTransactions(long atmNumber, int pin, double amount) {
		super();
		this.atmNumber = atmNumber;
		this.pin = pin;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "AtmTransactions [atmNumber=" + atmNumber + ", pin=" + pin + ", amount=" + amount + "]";
	}
	
	
	
	
	
}
