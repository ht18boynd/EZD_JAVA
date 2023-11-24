package com.ezd.repository;

import com.ezd.Dto.Role;
import com.ezd.models.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
	@Query("SELECT a FROM Auth a WHERE a.email = :email")
	Optional<Auth> findByEmail(@Param("email") String email);

	Auth findByRole(Role role);

	@Query("SELECT a FROM Auth a WHERE a.role = :role")
	List<Auth> getAllUsersList(@Param("role") Role role);

}
