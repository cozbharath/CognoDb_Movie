package com.movie.appmovie.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

	private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

	private final long expirationTime = 1000 * 60 * 60; // 1 hour

	// ==========================================
	// GENERATE TOKEN
	// ==========================================

	public String generateToken(String email) {

		Date now = new Date();

		Date expiryDate = new Date(now.getTime() + expirationTime);

		return Jwts.builder().subject(email).issuedAt(now).expiration(expiryDate).signWith(secretKey).compact();
	}

	// ==========================================
	// EXTRACT EMAIL
	// ==========================================

	public String extractUsername(String token) {

		return getClaims(token).getSubject();
	}

	// ==========================================
	// VALIDATE TOKEN
	// ==========================================

	public boolean validateToken(String token, String email) {

		try {

			String extractedEmail = extractUsername(token);

			return extractedEmail.equals(email) && !isTokenExpired(token);

		} catch (Exception e) {

			return false;
		}
	}

	// ==========================================
	// CHECK EXPIRATION
	// ==========================================

	private boolean isTokenExpired(String token) {

		Date expiration = getClaims(token).getExpiration();

		return expiration.before(new Date());
	}

	// ==========================================
	// GET CLAIMS
	// ==========================================

	private Claims getClaims(String token) {

		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}
}