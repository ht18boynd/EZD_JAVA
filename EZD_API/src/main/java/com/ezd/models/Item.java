package com.ezd.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false,name = "item_name",length = 100)
    private String name;
    
    @Column(nullable = false, name="item_image", length = 255)
    private String imageUrl;
    
    @Column(nullable = false, name="item_price")
    private BigDecimal price;
    
    @JsonBackReference
    @OneToMany(mappedBy = "item_purchase")
    private List<Purchase> purchases;
    
    @JsonBackReference
    @OneToMany(mappedBy = "items")
    private List<Donate> donates;
    
	public Item() {
		super();
	}
	
    
}
