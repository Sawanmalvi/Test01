package com.gwty_crm.util;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Jwtutil {

	private static final String SECRET_KEY = "afafafafafafafafasasafaabcdefghijklmnopqrstwxyzpqpqpqpqp";

	/**
	 * Validate the JWT token.
	 * 
	 * @param token JWT token to validate.
	 * @throws JwtException If the token is invalid.
	 */
	public void validateToken(final String token) {
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	/**
	 * Extract the username (subject) from the JWT token.
	 * 
	 * @param token JWT token.
	 * @return The subject (username) stored in the token.
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extract a claim from the token.
	 * 
	 * @param token          JWT token.
	 * @param claimsResolver Function to extract specific claim.
	 * @return Extracted claim value.
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	/**
	 * Get the signing key for JWT signature verification.
	 * 
	 * @return Signing key.
	 */
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Update token expiration time.
	 * 
	 * @param token    JWT token.
	 * @param duration New expiration duration.
	 * @return Updated JWT token.
	 */
//	public String updateTokenExpiration(String token, Duration duration) {
//		Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
//
//		Date now = new Date();
//		Date newExpiration = new Date(now.getTime() + duration.toMillis());
//
//		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(newExpiration)
//				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
//	}

	/**
	 * Extract company ID from JWT token.
	 * 
	 * @param token JWT token.
	 * @return Extracted company ID.
	 */
	public String extractCompanyId(String token) {
		return extractClaim(token, claims -> claims.get("companyId", String.class));
	}

	/**
	 * Extract client ID from JWT token.
	 * 
	 * @param token JWT token.
	 * @return Extracted client ID.
	 */
	public String extractClientId(String token) {
		return extractClaim(token, claims -> claims.get("clientId", String.class));
	}

	public String updateTokenExpiration(String token, Duration duration) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();

		claims.setExpiration(new Date(System.currentTimeMillis() + duration.toMillis()));

		return Jwts.builder().setClaims(claims).signWith(getSignKey()).compact();
	}
}
