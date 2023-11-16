package com.ezd.Dto;

import java.math.BigDecimal;
import java.util.List;

public class InfoDetailReponse {
	private Long id;
    private String name;

    private String accountName;

    private String email;
    private String address;
    private String country;
    private String phoneNumber;
    private String gender;
    private BigDecimal balance;
    private List<String> role;
    
    
    
	public InfoDetailReponse(Long id,String name, String accountName, String email, String address,
			String country, String phoneNumber, String gender, BigDecimal balance, List<String> role) {
		super();
		this.id = id;
		this.name = name;
		this.accountName = accountName;
		this.email = email;
		this.address = address;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.balance = balance;
		this.role = role;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<String> getRole() {
		return role;
	}
	public void setRole(List<String> role) {
		this.role = role;
	}
    
    
}
