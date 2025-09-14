package com.company.blog.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenHelper {

	// Secret key
	@Value("${jwt.secret}")
	private String secretKey;

	// Token validity
	@Value("${jwt.expiration}")
	private long jwtExpiration;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	// Generate token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration)).signWith(getSigningKey()).compact();
	}

	// Extract username from token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract expiration date from token
	public Date extractExpiration(String token) {
		return (Date) extractClaim(token, Claims::getExpiration);
	}

	// Extract single claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// For retrieving any information from token
	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
