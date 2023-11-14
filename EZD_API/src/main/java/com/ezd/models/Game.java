package com.ezd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameGame;
    private String imageName;
    
    @JsonBackReference
    @OneToMany(mappedBy = "game_product")
    private List<Product> products ;
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameGame() {
		return nameGame;
	}

	public void setNameGame(String nameGame) {
		this.nameGame = nameGame;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public List<LevelGame> getLevels() {
		return levels;
	}

	public void setLevels(List<LevelGame> levels) {
		this.levels = levels;
	}

	public List<PerfectRole> getRoles() {
		return roles;
	}

	public void setRoles(List<PerfectRole> roles) {
		this.roles = roles;
	}

	public List<Gender> getGenders() {
		return genders;
	}

	public void setGenders(List<Gender> genders) {
		this.genders = genders;
	}

	@ManyToMany
    @JoinTable(
        name = "game_level",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "level_id")
    )
    @JsonManagedReference
    private List<LevelGame> levels;

    @ManyToMany
    @JoinTable(
        name = "game_role",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private List<PerfectRole> roles;

    @ManyToMany
    @JoinTable(
        name = "game_gender",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "gender_id")
    )
    @JsonManagedReference
    private List<Gender> genders;

    // Constructors, getters, and setters
}

