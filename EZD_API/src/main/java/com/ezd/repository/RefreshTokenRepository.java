package com.ezd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ezd.models.Auth;
import com.ezd.models.RefreshToken;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);
	Optional<RefreshToken> findByAuth_Id(Long userId);
	
	@Modifying
	int deleteByAuth(Auth auth);
}
