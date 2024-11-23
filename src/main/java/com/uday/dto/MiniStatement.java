package com.uday.dto;

public class MiniStatement {
	
	private long accountNumber;

	public MiniStatement() {
		super();
	}

	public MiniStatement(long accountNumber) {
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
		return "MiniStatement [accountNumber=" + accountNumber + "]";
	}
	
	

}
