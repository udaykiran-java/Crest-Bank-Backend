package com.uday.model;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.uday.utility.AccountNumberGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;


@Entity
public class Bank {

	@Id
	private long accountNumber;
	private String name;
	private String fatherName;
	private String accountType;
	private long atmNumber;
	private String needsAtm = "no"; // Indicates if ATM is needed; default to "no"
	private int pin;
	private String branch;
	private String ifsc;
	private String date;
	private int age;
	private String dateOfBirth;
	private String email;
	private long mobileNumber;
	private long adharnumber;
	private String address;
	private String state;
	private String district;
	private int pinCode;
	private double amount;
	private String status;
	

	@OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistory> transactionHistory = new ArrayList<>();

    // Method to add a transaction to the list
    public void addTransaction(TransactionHistory transaction) {
        transactionHistory.add(transaction);
        transaction.setBank(this);
    }

	
    // PrePersist method to generate the account number before saving
    @PrePersist
    public void generateAccountDetails() {
        if (this.accountNumber == 0) {
            this.accountNumber = AccountNumberGenerator.generateAccountNumber();
        }
        // Generate ATM number if 'needsAtm' is "yes" and 'atmNumber' is 0
        
      if ("yes".equals(this.needsAtm) && this.atmNumber == 0) {
            this.atmNumber = AccountNumberGenerator.atmNumberGenarater();
        }
      // generate IFSC code
      if (branch == null || branch.length() < 2) {
          throw new IllegalArgumentException("Branch name must be at least 2 characters long.");
      }
      // Generate IFSC using the first 2 letters of the branch name and a static code
      this.ifsc = branch.substring(0, 2).toUpperCase(Locale.ROOT) + "009963";
    }
    
   


	public Bank() {
		super();
	}


	public Bank(long accountNumber, String name, String fatherName, String accountType, long atmNumber, String needsAtm,
			int pin, String branch, String ifsc, String date, int age, String dateOfBirth, String email,
			long mobileNumber, long adharnumber, String address, String state, String district, int pinCode,
			double amount, String status, List<TransactionHistory> transactionHistory) {
		super();
		this.accountNumber = accountNumber;
		this.name = name;
		this.fatherName = fatherName;
		this.accountType = accountType;
		this.atmNumber = atmNumber;
		this.needsAtm = needsAtm;
		this.pin = pin;
		this.branch = branch;
		this.ifsc = ifsc;
		this.date = date;
		this.age = age;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.adharnumber = adharnumber;
		this.address = address;
		this.state = state;
		this.district = district;
		this.pinCode = pinCode;
		this.amount = amount;
		this.status = status;
		this.transactionHistory = transactionHistory;
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


	public String getFatherName() {
		return fatherName;
	}


	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}


	public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public long getAtmNumber() {
		return atmNumber;
	}


	public void setAtmNumber(long atmNumber) {
		this.atmNumber = atmNumber;
	}


	public String getNeedsAtm() {
		return needsAtm;
	}


	public void setNeedsAtm(String needsAtm) {
		this.needsAtm = needsAtm;
	}


	public int getPin() {
		return pin;
	}


	public void setPin(int pin) {
		this.pin = pin;
	}


	public String getBranch() {
		return branch;
	}


	public void setBranch(String branch) {
		this.branch = branch;
	}


	public String getIfsc() {
		return ifsc;
	}


	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public long getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public long getAdharnumber() {
		return adharnumber;
	}


	public void setAdharnumber(long adharnumber) {
		this.adharnumber = adharnumber;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}


	public int getPinCode() {
		return pinCode;
	}


	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public List<TransactionHistory> getTransactionHistory() {
		return transactionHistory;
	}


	public void setTransactionHistory(List<TransactionHistory> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}


	@Override
	public String toString() {
		return "Bank [accountNumber=" + accountNumber + ", name=" + name + ", fatherName=" + fatherName
				+ ", accountType=" + accountType + ", atmNumber=" + atmNumber + ", needsAtm=" + needsAtm + ", pin="
				+ pin + ", branch=" + branch + ", ifsc=" + ifsc + ", date=" + date + ", age=" + age + ", dateOfBirth="
				+ dateOfBirth + ", email=" + email + ", mobileNumber=" + mobileNumber + ", adharnumber=" + adharnumber
				+ ", address=" + address + ", state=" + state + ", district=" + district + ", pinCode=" + pinCode
				+ ", amount=" + amount + ", status=" + status + ", transactionHistory=" + transactionHistory + "]";
	}
	
	

    


}