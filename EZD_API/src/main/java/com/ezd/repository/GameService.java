package com.ezd.repository;

import com.ezd.models.Game ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameService extends JpaRepository<Game , Long> {
	
}
