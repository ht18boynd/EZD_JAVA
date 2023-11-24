package com.ezd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long purchaseID;

	@ManyToOne
	@JoinColumn(name = "user_purchase_id", referencedColumnName = "id")
	private Auth auth_purchase;

	@ManyToOne
	@JoinColumn(name = "item_purchase_id", referencedColumnName = "id")
	private Item item_purchase;

	private int quantity;

	public Purchase() {
		super();
	}

	public Long getPurchaseID() {
		return purchaseID;
	}

	public void setPurchaseID(Long purchaseID) {
		this.purchaseID = purchaseID;
	}

	public Auth getAuth_purchase() {
		return auth_purchase;
	}

	public void setAuth_purchase(Auth auth_purchase) {
		this.auth_purchase = auth_purchase;
	}

	public Item getItem_purchase() {
		return item_purchase;
	}

	public void setItem_purchase(Item item_purchase) {
		this.item_purchase = item_purchase;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
