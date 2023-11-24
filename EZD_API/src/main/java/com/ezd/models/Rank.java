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
@Table(name = "ranks")
public class Rank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rankName", nullable = false, length = 50)
	private String rank_name;

	@Column(name = "minimumBalance", precision = 15, scale = 2)
	private BigDecimal minimum_balance;

	@Column(name = "maximumBalance", precision = 15, scale = 2)
	private BigDecimal maximum_balance;

	@Column(nullable = false, name = "avatar_frame_image", length = 255)
	private String avatar_frame_image;

	@Column(nullable = false, name = "background_image", length = 255)
	private String background_image;

	@JsonBackReference
	@OneToMany(mappedBy = "currentRank")
	private List<Auth> auth;

	public Rank() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRank_name() {
		return rank_name;
	}

	public void setRank_name(String rank_name) {
		this.rank_name = rank_name;
	}

	public BigDecimal getMinimum_balance() {
		return minimum_balance;
	}

	public void setMinimum_balance(BigDecimal minimum_balance) {
		this.minimum_balance = minimum_balance;
	}

	public String getAvatar_frame_image() {
		return avatar_frame_image;
	}

	public void setAvatar_frame_image(String avatar_frame_image) {
		this.avatar_frame_image = avatar_frame_image;
	}

	public String getBackground_image() {
		return background_image;
	}

	public void setBackground_image(String background_image) {
		this.background_image = background_image;
	}

	public List<Auth> getAuth() {
		return auth;
	}

	public void setAuth(List<Auth> auth) {
		this.auth = auth;
	}

	public BigDecimal getMaximum_balance() {
		return maximum_balance;
	}

	public void setMaximum_balance(BigDecimal maximum_balance) {
		this.maximum_balance = maximum_balance;
	}

}
