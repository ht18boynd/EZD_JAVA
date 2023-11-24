package com.ezd.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.Dto.JwtAuthenticationResponse;
import com.ezd.Dto.RefreshTokenRequest;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;

import jakarta.mail.MessagingException;

public interface AuthenticationService extends JpaRepository<Auth, Long> {

	Auth signup(SignUpRequest signUpRequest) throws MessagingException;

	JwtAuthenticationResponse signin(SignInRequest signInRequest);

	JwtAuthenticationResponse signinAdmin(SignInRequest signInRequest);

	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

	void resetPassword(String email) throws MessagingException;
	
    void updatePassword(String userEmail, String currentPassword, String newPassword);

    Optional<Auth> getAuthById(Long id);
}
