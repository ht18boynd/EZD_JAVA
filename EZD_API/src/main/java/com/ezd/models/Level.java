package com.ezd.models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data



@Entity
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Tên cấp độ, ví dụ: "Bạc", "Vàng", "Đồng", ...

    @ManyToMany(mappedBy = "levels")
    private List<Game> games; // Danh sách các trò chơi thuộc cấp độ này

    // Constructors, getters, and setters
}