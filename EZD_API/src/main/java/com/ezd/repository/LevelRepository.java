package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezd.models.LevelGame;

@Repository
public interface LevelRepository extends JpaRepository<LevelGame, Long> {
}

