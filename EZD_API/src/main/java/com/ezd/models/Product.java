package com.ezd.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ElementCollection
	private List<String> imageUrls; // Danh sách URL hình ảnh
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_prduct_id", referencedColumnName = "id")
	private Auth user_game; // Tham chiếu đến người dùng

	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "game_product_id", referencedColumnName = "id")
	 private Game game; // Danh sách game tham chiếu

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	@JsonBackReference
	private PerfectRole role;

	@ManyToOne
	@JoinColumn(name = "level_id", referencedColumnName = "id")
	@JsonBackReference
	private LevelGame level;

	@ManyToOne
	@JoinColumn(name = "gender_id", referencedColumnName = "id")
	@JsonBackReference
	private Gender gender;
	private StatusAccount status;
	private String decription;
	private BigDecimal price;
	private LocalDateTime createdProduct;

}
