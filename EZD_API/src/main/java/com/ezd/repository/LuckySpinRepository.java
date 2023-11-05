package com.ezd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezd.models.LuckySpin;

public interface LuckySpinRepository  extends JpaRepository<LuckySpin, Long>{
	
	@Query("SELECT ls FROM LuckySpin ls WHERE ls.user_lucky.id = :userId")
	List<LuckySpin> findLuckySpinsByUserId(@Param("userId") Long userId);



}
