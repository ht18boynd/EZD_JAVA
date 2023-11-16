package com.ezd.repository;

import com.ezd.models.Auth;
import com.ezd.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
//	@Query("SELECT a FROM Auth a WHERE a.email = :email")
//	Optional<Auth> findByEmail(@Param("email") String email);
	Optional<Auth> findByEmail(String email);
	Optional<Auth> findByUsername(String username);
	boolean existsByAccountName(String accountName);

	Boolean existsByEmail(String email);
}
