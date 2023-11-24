package com.ezd.models;

import java.time.LocalDateTime;

import com.ezd.Dto.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity

public class BecomeIdol {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String fullName;
	private LocalDateTime birthDay;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_become_id", referencedColumnName = "id")
	private Auth user_become; // Tham chiếu đến người dùng

	private String street;
	private String img_before;
	private String img_after;
	private String img_avarta;
	private String img_rank;
	private String decription;

	private String url_faceBook;
	private String url_youtube;
	private LocalDateTime becomeTime;
	private Status status;
	
	private boolean checkedByAdmin;
	private LocalDateTime adminCheckTime;

	public BecomeIdol(Long id, String fullName, LocalDateTime birthDay, Auth user_become, String street,
			String img_before, String img_after, String img_avarta, String img_rank, String decription,
			String url_faceBook, String url_youtube, LocalDateTime becomeTime, Status status, boolean checkedByAdmin,
			LocalDateTime adminCheckTime) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.birthDay = birthDay;
		this.user_become = user_become;
		this.street = street;
		this.img_before = img_before;
		this.img_after = img_after;
		this.img_avarta = img_avarta;
		this.img_rank = img_rank;
		this.decription = decription;

		this.url_faceBook = url_faceBook;
		this.url_youtube = url_youtube;
		this.becomeTime = becomeTime;
		this.status = status;
		this.checkedByAdmin = checkedByAdmin;
		this.adminCheckTime = adminCheckTime;
	}

	public BecomeIdol() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDateTime getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(LocalDateTime birthDay) {
		this.birthDay = birthDay;
	}

	public Auth getUser_become() {
		return user_become;
	}

	public void setUser_become(Auth user_become) {
		this.user_become = user_become;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getImg_before() {
		return img_before;
	}

	public void setImg_before(String img_before) {
		this.img_before = img_before;
	}

	public String getImg_after() {
		return img_after;
	}

	public void setImg_after(String img_after) {
		this.img_after = img_after;
	}

	public String getImg_avarta() {
		return img_avarta;
	}

	public void setImg_avarta(String img_avarta) {
		this.img_avarta = img_avarta;
	}

	public String getImg_rank() {
		return img_rank;
	}

	public void setImg_rank(String img_rank) {
		this.img_rank = img_rank;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public String getUrl_faceBook() {
		return url_faceBook;
	}

	public void setUrl_faceBook(String url_faceBook) {
		this.url_faceBook = url_faceBook;
	}

	public String getUrl_youtube() {
		return url_youtube;
	}

	public void setUrl_youtube(String url_youtube) {
		this.url_youtube = url_youtube;
	}

	public LocalDateTime getBecomeTime() {
		return becomeTime;
	}

	public void setBecomeTime(LocalDateTime becomeTime) {
		this.becomeTime = becomeTime;
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
	
	

}
