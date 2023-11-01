package com.ezd.models;



import com.ezd.Dto.BannerStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String image;
    private BannerStatus status;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	

	public Banner(Long id, String name, String title, String image, BannerStatus status) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.image = image;
		this.status = status;
	}
	public Banner() {
		// TODO Auto-generated constructor stub
	}
    // Constructors, getters, and setters
	public BannerStatus getStatus() {
		return status;
	}
	public void setStatus(BannerStatus status) {
		this.status = status;
	}
	
}
