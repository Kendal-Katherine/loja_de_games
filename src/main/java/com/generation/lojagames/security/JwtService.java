package com.generation.lojagames.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


//FAZ VERIFICAÇÃO DO TOKEN ... ESSA CLASSE LIDA COM A CRIAÇÃO E VERIFICAÇÃO DE TOKEN
@Component
public class JwtService {
	//SECRET TEM QUE ESTÁ ESCONDIDA E NÃO PODE APARECER NO CÓDIGO, NESSE CASO DE APRENDIZADO PODE
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	//payload extrai o nome do usuário
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	//payload extrai a data de expiração do token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	//payload verifica se o token está expirado
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	//payload verifica se o token é válido
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	//ELE AQUI FABRICA O TOKEN 
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

}