package com.ezd.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name ="ranks")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rank_id;

    @Column(name = "rankName",nullable = false,length = 50)
    private String rank_name;

    @Column(name="minimumBalance",precision = 10, scale = 2)
    private BigDecimal minimum_balance;

    @Column(nullable = false, name="avatar_frame_image", length = 255)
    private String avatar_frame_image;

    @Column(nullable = false, name="background_image", length = 255)
    private String background_image;
    
	public Rank() {
		super();
	}

	public Long getRank_id() {
		return rank_id;
	}

	public void setRank_id(Long rank_id) {
		this.rank_id = rank_id;
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
    
    
}
