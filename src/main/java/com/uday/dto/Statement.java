package com.uday.dto;

public class Statement {
	
	private long accountNumber;

	public Statement() {
		super();
	}

	public Statement(long accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "Statement [accountNumber=" + accountNumber + "]";
	}
	
	
	
	

}
