package com.ezd.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserDetailsService userDetailsService();
}
