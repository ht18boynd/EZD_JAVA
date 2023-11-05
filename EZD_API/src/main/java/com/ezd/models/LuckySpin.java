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

@Entity
@Data
public class LuckySpin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @JsonManagedReference

    @ManyToOne
    @JoinColumn(name = "user_lucky_id", referencedColumnName = "id")
    private Auth user_lucky; // Tham chiếu đến người dùng
    private BigDecimal point;
	private LocalDateTime luckyTime;
	
	public LuckySpin() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Auth getUser_lucky() {
		return user_lucky;
	}
	public void setUser_lucky(Auth user_lucky) {
		this.user_lucky = user_lucky;
	}
	public BigDecimal getPoint() {
		return point;
	}
	public void setPoint(BigDecimal point) {
		this.point = point;
	}
	public LocalDateTime getLuckyTime() {
		return luckyTime;
	}
	public void setLuckyTime(LocalDateTime luckyTime) {
		this.luckyTime = luckyTime;
	}
	public LuckySpin(Long id, Auth user_lucky, BigDecimal point, LocalDateTime luckyTime) {
		super();
		this.id = id;
		this.user_lucky = user_lucky;
		this.point = point;
		this.luckyTime = luckyTime;
	} 
	
	

}
