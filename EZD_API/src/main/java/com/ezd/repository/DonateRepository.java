package com.ezd.repository;

import com.ezd.models.Donate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DonateRepository extends JpaRepository<Donate, Long> {
	
    @Query("SELECT d FROM Donate d WHERE d.user_from.id = :userFromId ")

	
    List<Donate> findByUserFromIdOrUserToId(Long userFromId);
}
