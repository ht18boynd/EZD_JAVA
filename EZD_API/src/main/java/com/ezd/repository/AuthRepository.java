package com.ezd.repository;

import com.ezd.models.Auth;
import com.ezd.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
	 @Query("SELECT a FROM Auth a WHERE a.email = :email")
	 Optional<Auth> findByEmail(@Param("email") String email);
  
    Auth findByRole (Role role);
}