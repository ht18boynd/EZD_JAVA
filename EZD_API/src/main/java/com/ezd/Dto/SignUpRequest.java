package com.ezd.Dto;


import com.ezd.models.StatusAccount;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String address;
    private String country;
    private String phoneNumber;
    private String gender;
    private BigDecimal balance;
    private StatusAccount status;
    private Role role;
    private String birthDay;
    private LocalDateTime createdDate;
    public SignUpRequest() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public StatusAccount getStatus() {
		return status;
	}
	public void setStatus(StatusAccount status) {
		this.status = status;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public SignUpRequest(String name, String email, String password, String address, String country, String phoneNumber,
			String gender, BigDecimal balance, StatusAccount status, Role role, String birthDay,
			LocalDateTime createdDate) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.balance = balance;
		this.status = status;
		this.role = role;
		this.birthDay = birthDay;
		this.createdDate = createdDate;
	}
     
}
