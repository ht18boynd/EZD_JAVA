package com.ezd.service.Impl;


import com.ezd.repository.AuthRepository;
import com.ezd.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
    private AuthRepository userRepository;

    @Override
    public UserDetailsService userDetailsService (){
        return  new UserDetailsService () {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            }
        };
    }
}
