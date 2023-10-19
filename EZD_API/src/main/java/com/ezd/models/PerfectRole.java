package com.ezd.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
@Data
@Entity
public class PerfectRole {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String name; // Tên vị trí : "top", "mid", "bot", ...

	    @ManyToMany(mappedBy = "roles")
	    private List<Game> games; // Danh sách các trò chơi thuộc vị trí này
}
