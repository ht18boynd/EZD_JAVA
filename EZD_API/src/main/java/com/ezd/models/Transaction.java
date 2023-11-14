package com.ezd.models;

import jakarta.persistence.*;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.ezd.Dto.Status;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @JsonManagedReference

    @ManyToOne
    @JoinColumn(name = "user_transaction_id", referencedColumnName = "id")
    private Auth user_transaction; // Tham chiếu đến người dùng

	private BigDecimal amount;
	private LocalDateTime transactionTime;
    private Status status;
    
    private boolean checkedByAdmin;
    private LocalDateTime adminCheckTime;


	public Transaction(Long id, Auth user_transaction, BigDecimal amount, LocalDateTime transactionTime, Status status,
			boolean checkedByAdmin, LocalDateTime adminCheckTime) {
		super();
		this.id = id;
		this.user_transaction = user_transaction;
		this.amount = amount;
		this.transactionTime = transactionTime;
		this.status = status;
		this.checkedByAdmin = checkedByAdmin;
		this.adminCheckTime = adminCheckTime;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Auth getUser_transaction() {
		return user_transaction;
	}


	public void setUser_transaction(Auth user_transaction) {
		this.user_transaction = user_transaction;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}


	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public boolean isCheckedByAdmin() {
		return checkedByAdmin;
	}


	public void setCheckedByAdmin(boolean checkedByAdmin) {
		this.checkedByAdmin = checkedByAdmin;
	}


	public LocalDateTime getAdminCheckTime() {
		return adminCheckTime;
	}


	public void setAdminCheckTime(LocalDateTime adminCheckTime) {
		this.adminCheckTime = adminCheckTime;
	}


	public Transaction() {
		// TODO Auto-generated constructor stub
	}
}
