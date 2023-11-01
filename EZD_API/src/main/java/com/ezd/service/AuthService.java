package com.ezd.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
    UserDetailsService userDetailsService();
}
