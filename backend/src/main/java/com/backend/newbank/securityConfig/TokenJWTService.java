package com.backend.newbank.securityConfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.backend.newbank.exception.ExcepitionTokenGenerated;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenJWTService {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public String generateToken(Long accountId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(accountId.toString())
                    .withExpiresAt(LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ExcepitionTokenGenerated("Token não pode ser gerado");
        }
    }

    public Long validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
            String subject = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token)
                    .getSubject();

            return Long.parseLong(subject); // Convertendo o subject para Long
        } catch (JWTVerificationException | NumberFormatException exception) {
            return null; // Token inválido ou erro de formatação
        }
    }
}
