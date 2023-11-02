package com.ezd.models;

import jakarta.persistence.*;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private Auth user; // Liên kết với thông tin người dùng

    private BigDecimal amount;
    private String currency;
    private Date timestamp;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Auth getUser() {
		return user;
	}
	public void setUser(Auth user) {
		this.user = user;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}public Transaction() {
		// TODO Auto-generated constructor stub
	}
	public Transaction(Long id, Auth user, BigDecimal amount, String currency, Date timestamp) {
		super();
		this.id = id;
		this.user = user;
		this.amount = amount;
		this.currency = currency;
		this.timestamp = timestamp;
	}


    // Các getter và setter
}
