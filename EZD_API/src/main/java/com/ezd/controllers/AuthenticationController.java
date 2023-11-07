package com.ezd.controllers;


import com.ezd.Dto.JwtAuthenticationResponse;
import com.ezd.Dto.RefreshTokenRequest;
import com.ezd.Dto.Role;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;
import com.ezd.models.Game;
import com.ezd.models.Transaction;
import com.ezd.repository.AuthRepository;
import com.ezd.service.AuthenticationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	@Autowired 
	private final  AuthRepository authRepository;
    private  final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Auth> signup(@RequestBody SignUpRequest signUpRequest) {
        return  ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public  ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest) {
        return  ResponseEntity.ok(authenticationService.signin(signInRequest));
    }
    @PostMapping("/signinAdmin")
    public  ResponseEntity<JwtAuthenticationResponse> signinAdmin(@RequestBody SignInRequest signInRequest) {
        return  ResponseEntity.ok(authenticationService.signinAdmin(signInRequest));
    }

    @PostMapping("/refresh")
    public  ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return  ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
    @GetMapping("/findByEmail")
    public ResponseEntity<Optional<Auth>> findByEmail(@RequestParam String email) {
        Optional<Auth> auth = authRepository.findByEmail(email);

        if (auth != null) {
            return ResponseEntity.ok(auth);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/")
    public List<Auth> getAllUser(@RequestParam("role") Role role) {
        return  authRepository.getAllUsersList(role);
    }
  
}
