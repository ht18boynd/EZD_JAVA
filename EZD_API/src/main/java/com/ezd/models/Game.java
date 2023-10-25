package com.ezd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameGame;
    private String imageName;

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

