package com.uday.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    private Bank bank;

    private LocalDateTime transactionDate;
    private String transactionType; // e.g., "Credit" or "Debit"
    private double amount;
    private double availableBalance;
    private String Description;
	public TransactionHistory() {
		super();
	}
	public TransactionHistory(Long transactionId, Bank bank, LocalDateTime transactionDate, String transactionType,
			double amount, double availableBalance, String description) {
		super();
		this.transactionId = transactionId;
		this.bank = bank;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
		this.amount = amount;
		this.availableBalance = availableBalance;
		Description = description;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@Override
	public String toString() {
		return "TransactionHistory [transactionId=" + transactionId + ", bank=" + bank + ", transactionDate="
				+ transactionDate + ", transactionType=" + transactionType + ", amount=" + amount
				+ ", availableBalance=" + availableBalance + ", Description=" + Description + "]";
	}
	
	
    
    
    
    
    
}
