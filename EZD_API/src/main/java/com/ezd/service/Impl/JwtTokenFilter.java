package com.ezd.service.Impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ezd.service.AuthDetailServiceImpl;
import com.ezd.service.AuthDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Bộ lọc xác thực các Token
public class JwtTokenFilter extends OncePerRequestFilter {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	AuthDetailServiceImpl authDetailServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				
				String email = jwtUtils.getEmailFromJwtToken(jwt);
				
				UserDetails userDetails = authDetailServiceImpl.loadUserByUsername(email);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		filterChain.doFilter(request, response);
	}

	// Chuyển dổi - parseJwt
	private String parseJwt(HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromCookies(request);
		return jwt;
	}
}
