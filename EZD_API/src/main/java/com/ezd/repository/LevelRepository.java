package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ezd.models.LevelGame;

public interface LevelRepository extends JpaRepository<LevelGame, Long> {

}
