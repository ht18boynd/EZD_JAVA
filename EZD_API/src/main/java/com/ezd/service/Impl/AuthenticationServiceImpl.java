package com.ezd.service.Impl;

import com.ezd.TokenRefreshException;
import com.ezd.Dto.InfoDetailReponse;

import com.ezd.Dto.MessageResponse;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;
import com.ezd.models.ERole;
import com.ezd.models.RefreshToken;
import com.ezd.models.Role;

import com.ezd.repository.AuthRepository;
import com.ezd.repository.RoleRepository;
import com.ezd.service.AuthDetailsImpl;
import com.ezd.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.HashSet;
import java.util.List;

import java.util.Set;

import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AuthRepository authRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	RefreshTokenService refreshTokenService;

	public ResponseEntity<?> signup(SignUpRequest signUp) {
		if (authRepository.existsByEmail(signUp.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
		}
		if (authRepository.existsByAccountName(signUp.getAccountName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: AccountName is already taken!"));
		}
		BigDecimal balance = signUp.getBalance();
		// Create new User, Auth
		Auth auth = new Auth(signUp.getName(), signUp.getAccountName(), signUp.getEmail(),
				encoder.encode(signUp.getPassword()), signUp.getAddress(), signUp.getCountry(), signUp.getPhoneNumber(),
				signUp.getGender(), balance);

		Set<String> strRole = signUp.getRole();

		Set<Role> roles = new HashSet<>();

		if (strRole == null) {
			Role authRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(authRole);
		} else {
			strRole.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
					
				case "prov":
					Role provRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(provRole);
					break;
					
				default:
					Role authRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(authRole);
				}
			});
		}
		auth.setRoles(roles);
		authRepository.save(auth);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	public ResponseEntity<?> signin(SignInRequest signInRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		AuthDetailsImpl authDetails = (AuthDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(authDetails);

		BigDecimal balance = authDetails.getBalance();

		// Chuyển đổi Set<Role> sang List<Role>
		List<String> roles = authDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		// refresh TOken
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(authDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(new InfoDetailReponse(authDetails.getId(), authDetails.getUsername(),
						authDetails.getAccountName(), authDetails.getEmail(), authDetails.getAddress(),
						authDetails.getCountry(), authDetails.getPhoneNumber(), authDetails.getGender(), balance,
						roles));
	}

	public ResponseEntity<?> logout() {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principle.toString() != "anonymousUser") {
			Long authId = ((AuthDetailsImpl) principle).getId();
			refreshTokenService.deleteByUserId(authId);
		}

		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(new MessageResponse("You've been signed out!"));
	}

	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

		if ((refreshToken != null) && (refreshToken.length() > 0)) {
			return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration)
					.map(RefreshToken::getAuth).map(auth -> {
						ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(auth);

						return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
								.body(new MessageResponse("Token is refreshed successfully!"));
					}).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
	}
}
