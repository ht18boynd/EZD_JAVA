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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Auth getUser_product() {
		return user_product;
	}
	public void setUser_product(Auth user_product) {
		this.user_product = user_product;
	}
	public Game getGame_product() {
		return game_product;
	}
	public void setGame_product(Game game_product) {
		this.game_product = game_product;
	}
	public PerfectRole getRole_product() {
		return role_product;
	}
	public void setRole_product(PerfectRole role_product) {
		this.role_product = role_product;
	}
	public LevelGame getLevel_product() {
		return level_product;
	}
	public void setLevel_product(LevelGame level_product) {
		this.level_product = level_product;
	}
	public Gender getGender_product() {
		return gender_product;
	}
	public void setGender_product(Gender gender_product) {
		this.gender_product = gender_product;
	}
	public String getImg_product() {
		return img_product;
	}
	public void setImg_product(String img_product) {
		this.img_product = img_product;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
	public StatusAccount getStatus() {
		return status;
	}
	public void setStatus(StatusAccount status) {
		this.status = status;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	

	
	
	
	
	
}
