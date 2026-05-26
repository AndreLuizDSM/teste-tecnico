package com.andre.testetecnico.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtil {


    // Pegando key no application properties
    @Value("${jwt.secretkey}")
    private String secretKey;

    public SecretKey getSecretKey(){
        byte[] key = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    // Gera um token JWT com o email do usuário e validade de 1 hora
    public String generateToken(String usernameEmail) {
        return Jwts.builder()
                .subject(usernameEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSecretKey())
                .compact();
    }

    // Extrai as claims do token JWT (informações adicionais do token)
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token) // Analisa o token JWT e obtém as claims
                .getPayload();
    }

    // Extrai o nome de usuário do token JWT
    public String extrairEmailToken(String token) {

        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    public boolean isTokenExpired(String token) {

        return extractClaims(token).getExpiration().before(new Date());
    }

    // Valida o token JWT verificando o nome de usuário e se o token não está expirado
    public boolean validateToken(String token, String username) {

        final String extractedUsername = extrairEmailToken(token);

        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
