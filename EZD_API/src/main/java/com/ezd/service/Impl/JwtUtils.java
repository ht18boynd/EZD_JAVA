package com.ezd.service.Impl;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ezd.models.Auth;
import com.ezd.service.AuthDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${app.jwtCookieName}")
	private String jwtCookie;
	
	@Value("${app.jwtRefreshCookieName}")
	private String jwtRefreshCookie;
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)); // chuyển đổi chuổi base64
	}

	// Lấy cookie từ form
	public String getJwtFromCookies(HttpServletRequest request) {
		return getCookieValueByEmail(request, jwtCookie);
	}

	// Generate cookie
	public ResponseCookie generateJwtCookie(AuthDetailsImpl authPrincipal) {
		String jwt = generateTokenFromAuth(authPrincipal.getEmail());
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true)
				.build();
		return cookie;
	}

	public ResponseCookie generateJwtCookie(Auth auth) {
		String jwt = generateTokenFromAuth(auth.getEmail());
		return generateCookie(jwtCookie, jwt, "/api");
	}

	// Làm sạch cookie
	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
		return cookie;
	}

	// Lấy thông tin AccountName từ JWT TOken
	public String getEmailFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key())
				.build().parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	// Xác thực JwtToken
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateTokenFromAuth(String email) {
		long expirationTimeMillis = System.currentTimeMillis();
		Date expirationDate = new Date(expirationTimeMillis + jwtExpirationMs);
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(expirationDate)
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}
	
	public String getJwtRefreshFromCookies(HttpServletRequest request) {
		return getCookieValueByEmail(request, jwtRefreshCookie);
	}

	public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
		return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth/refreshtoken");
	}
	public ResponseCookie getCleanJwtRefreshCookie() {
		    ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null)
		    		.path("/api/auth/refreshtoken")
		    		.build();
		   return cookie;
	}	
	
	private ResponseCookie generateCookie(String email, String value, String path) {
		ResponseCookie cookie = ResponseCookie.from(email, value).path(path).maxAge(24 * 60 * 60).httpOnly(true).build();
		return cookie;
	}

	private String getCookieValueByEmail(HttpServletRequest request, String email) {
		Cookie cookie = WebUtils.getCookie(request, email);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}
}
