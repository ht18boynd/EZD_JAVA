package com.ezd.service.Impl;

import com.ezd.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
	public String generateToken(UserDetails userDetails) {
		long expirationTimeMillis = System.currentTimeMillis() + (1000L * 60 * 60 * 24); // 24 giờ
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(expirationTimeMillis)).signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		long expirationTimeMillis = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 7); // 7 ngày
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(expirationTimeMillis))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}

	private Key getSigningKey() {
		byte[] key = Decoders.BASE64.decode(
				"YourBase64EncodedSecretKey237e293e02938e9238e0923890w809283e0928309823e09283e09238e0293809e839e8209809e");
		return Keys.hmacShaKeyFor(key);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUserName(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}
