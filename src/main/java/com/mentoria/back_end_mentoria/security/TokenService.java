package com.mentoria.back_end_mentoria.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mentoria.back_end_mentoria.usuario.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expiracao = dataExpiracao();
        SecretKey chaveSecreta = getChaveDeAssinatura();
    
        return Jwts.builder()
                .issuer("API MentorIA")
                .subject(usuario.getUsername())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(expiracao))
                .signWith(chaveSecreta)
                .compact();
                
    }

    public String getSubject(String tokenJWT){
        try{
            SecretKey chaveSecreta = getChaveDeAssinatura();

            Claims claims = Jwts.parser()
                    .verifyWith(chaveSecreta)
                    .build()
                    .parseSignedClaims(tokenJWT)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token JWT inválido ou expirado", e);
        }
    }

    private SecretKey getChaveDeAssinatura() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Instant dataExpiracao() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }

}
