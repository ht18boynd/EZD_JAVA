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

	

}
