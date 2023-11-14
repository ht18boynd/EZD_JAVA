package com.ezd.repository;

import com.ezd.models.Game ;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GameRepository extends JpaRepository<Game , Long> {

}
