package com.ezd.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import net.bytebuddy.utility.nullability.MaybeNull;

@Data
@Entity
public class Donate {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;
    
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_from_id", referencedColumnName = "id")
    private Auth user_from; // Tham chiếu đến người dùng

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_to_id", referencedColumnName = "id")
    private Auth user_to; // Tham chiếu đến người dùng
    
    @ManyToOne
    @JoinColumn(name = "item_donate", referencedColumnName = "id")
    private Item items;
    
    private int quantity;
    
    private LocalDateTime transactionDate;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public Auth getUser_from() {
		return user_from;
	}


	public void setUser_from(Auth user_from) {
		this.user_from = user_from;
	}


	public Auth getUser_to() {
		return user_to;
	}


	public void setUser_to(Auth user_to) {
		this.user_to = user_to;
	}


	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Item getItems() {
		return items;
	}


	public void setItems(Item items) {
		this.items = items;
	}
    
    
}
