package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezd.models.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
}

