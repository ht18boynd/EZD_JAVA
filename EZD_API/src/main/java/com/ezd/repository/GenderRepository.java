package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Gender;

public interface GenderRepository   extends JpaRepository<Gender , Long> {

}
