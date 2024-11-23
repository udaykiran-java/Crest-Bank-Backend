package com.uday.dto;

public class TransferAmount {
	
	private long fromAccountNumber;
	private String name;
	private long mobileNumber;
	private long toAccountNumber;
	private double amount;
	public TransferAmount() {
		super();
	}
	public TransferAmount(long fromAccountNumber, String name, long mobileNumber, long toAccountNumber, double amount) {
		super();
		this.fromAccountNumber = fromAccountNumber;
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
	}
	public long getFromAccountNumber() {
		return fromAccountNumber;
	}
	public void setFromAccountNumber(long fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
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
		return "TransferAmount [fromAccountNumber=" + fromAccountNumber + ", name=" + name + ", mobileNumber="
				+ mobileNumber + ", toAccountNumber=" + toAccountNumber + ", amount=" + amount + "]";
	}
	
	
	
}
