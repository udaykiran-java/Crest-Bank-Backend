package com.uday.dto;

public class UpdateAccount {
	
	private long accountNumber;
	private String ifsc;
	private String name;
	private String fatherName;
	private int age;
	private String dateOfBirth;
	private String email;
	private long mobileNumber;
	private String address;
	private String state;
	private String district;
	private long adharnumber;
	public UpdateAccount() {
		super();
	}
	public UpdateAccount(long accountNumber, String ifsc, String name, String fatherName, int age, String dateOfBirth,
			String email, long mobileNumber, String address, String state, String district, long adharnumber) {
		super();
		this.accountNumber = accountNumber;
		this.ifsc = ifsc;
		this.name = name;
		this.fatherName = fatherName;
		this.age = age;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.state = state;
		this.district = district;
		this.adharnumber = adharnumber;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
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
	public long getAdharnumber() {
		return adharnumber;
	}
	public void setAdharnumber(long adharnumber) {
		this.adharnumber = adharnumber;
	}
	@Override
	public String toString() {
		return "UpdateAccount [accountNumber=" + accountNumber + ", ifsc=" + ifsc + ", name=" + name + ", fatherName="
				+ fatherName + ", age=" + age + ", dateOfBirth=" + dateOfBirth + ", email=" + email + ", mobileNumber="
				+ mobileNumber + ", address=" + address + ", state=" + state + ", district=" + district
				+ ", adharnumber=" + adharnumber + "]";
	}
	
	
	
	

}
