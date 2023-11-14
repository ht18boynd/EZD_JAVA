package com.ezd.models;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_product_id", referencedColumnName = "id")
    private Auth user_product; 
	
	@JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "game_product_id", referencedColumnName = "id")
    private Game game_product; 
	
	@JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "role_product_id", referencedColumnName = "id")
    private PerfectRole role_product; 
	
	@JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "level_product_id", referencedColumnName = "id")
    private LevelGame level_product; 
	
	
	@JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "gender_product_id", referencedColumnName = "id")
    private Gender gender_product; 
	
	
	private String img_product;
	private BigDecimal price;
	
	private int hour;


	private String decription ; 
	
	private StatusAccount status;
	private Date created_date;
	


	
	
	
	
}
