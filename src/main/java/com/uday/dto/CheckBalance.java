package com.uday.dto;

public class CheckBalance {
	
	private long accountNumber;
	private long mobileNumber;
	public CheckBalance() {
		super();
	}
	public CheckBalance(long accountNumber, long mobileNumber) {
		super();
		this.accountNumber = accountNumber;
		this.mobileNumber = mobileNumber;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	@Override
	public String toString() {
		return "CheckBalance [accountNumber=" + accountNumber + ", mobileNumber=" + mobileNumber + "]";
	}
	
	

}
