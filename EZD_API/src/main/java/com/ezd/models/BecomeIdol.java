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

}
