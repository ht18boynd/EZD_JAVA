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

@Data
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameGame;
    private String imageName; // Trường để lưu trữ tên tệp hình ảnh

    @ManyToMany
    @JoinTable(
        name = "game_level", // Tên bảng nối
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "level_id")
    )
    private List<Level> levels; // Danh sách các cấp độ
    @ManyToMany
    @JoinTable(
        name = "game_role", // Tên bảng nối
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<PerfectRole> roles; // Danh sách các cấp độ
    @ManyToMany
    @JoinTable(
        name = "game_gender", // Tên bảng nối
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "gender_id")
    )
    private List<Gender> genders; // Danh sách các giới tính

    // Constructors, getters, and setters
}
