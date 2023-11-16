package com.ezd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezd.models.Auth;
import com.ezd.repository.AuthRepository;

@Service
public class AuthDetailServiceImpl implements UserDetailsService {
	@Autowired
	AuthRepository authRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Auth auth = authRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email not fount"));
		return AuthDetailsImpl.build(auth);
	}
	
}
