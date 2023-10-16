package com.ezd.dto;

import com.ezd.common.Role;
import com.ezd.config.JwtService;
import com.ezd.models.Admin;
import com.ezd.repository.IAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final IAdmin repository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    public  AuthenticationResponse register(RegisterRequest request) {
        var admin = Admin.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        repository.save(admin);
        var jwtToken = jwtService.generateToken(admin);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var admin = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(admin);

        return  AuthenticationResponse.builder().token(jwtToken).build();

    }
}
