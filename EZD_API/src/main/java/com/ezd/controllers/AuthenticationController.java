package com.ezd.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.Dto.InfoDetailReponse;
import com.ezd.Dto.MessageResponse;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.RefreshToken;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.RoleRepository;
import com.ezd.service.AuthDetailsImpl;
import com.ezd.service.RefreshTokenService;
import com.ezd.service.Impl.AuthenticationServiceImpl;
import com.ezd.service.Impl.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AuthenticationServiceImpl authenticationServiceImpl;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
		return ResponseEntity.ok(authenticationServiceImpl.signup(signUpRequest));
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signin) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(signin.getEmail(), signin.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		AuthDetailsImpl authDetails = (AuthDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(authDetails);
		BigDecimal balance = authDetails.getBalance();
		List<String> roles = authDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(authDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(new InfoDetailReponse(authDetails.getId(), authDetails.getUsername(),
						authDetails.getAccountName(), authDetails.getEmail(), authDetails.getAddress(),
						authDetails.getCountry(), authDetails.getPhoneNumber(), authDetails.getGender(), balance,
						roles));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
//		return ResponseEntity.ok(authenticationServiceImpl.logout());
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

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refresh(HttpServletRequest request) {
		return ResponseEntity.ok(authenticationServiceImpl.refreshToken(request));
	}

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_PROVIDER') or hasAuthority('ROLE_ADMIN')")
	public String authAccess() {
		return "User Content.";
	}

	@GetMapping("/provider")
	@PreAuthorize("hasAuthority('ROLE_PROVIDER')")
	public String providerAccess() {
		return "Provider Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
