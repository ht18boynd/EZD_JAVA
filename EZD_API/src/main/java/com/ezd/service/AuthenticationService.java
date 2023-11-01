package com.ezd.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezd.Dto.JwtAuthenticationResponse;
import com.ezd.Dto.RefreshTokenRequest;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;
import com.ezd.repository.AuthRepository;

public interface AuthenticationService extends JpaRepository<Auth, Long> {
	
    
    Auth signup(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signin(SignInRequest signInRequest);
    JwtAuthenticationResponse signinAdmin(SignInRequest signInRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
	