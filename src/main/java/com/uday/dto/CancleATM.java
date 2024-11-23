package com.uday.dto;

public class CancleATM {
	
	private long accountNumber;
	private String name;
	private long mobileNumber;
	public CancleATM() {
		super();
	}
	public CancleATM(long accountNumber, String name, long mobileNumber) {
		super();
		this.accountNumber = accountNumber;
		this.name = name;
		this.mobileNumber = mobileNumber;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
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
	@Override
	public String toString() {
		return "CancleATM [accountNumber=" + accountNumber + ", name=" + name + ", mobileNumber=" + mobileNumber + "]";
	}
	
	

}
