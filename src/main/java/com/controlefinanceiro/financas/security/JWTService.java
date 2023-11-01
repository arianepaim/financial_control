package com.controlefinanceiro.financas.security;

import com.controlefinanceiro.financas.shared.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JWTService {

    private static final String privateKeyJWT = "secretKey";

    public String generateToken(Authentication authentication) {

        int expirationTime = 86400000;

        Date expirationDate = new Date(new Date().getTime() + expirationTime);

        UserDTO userDto = (UserDTO) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDto.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, privateKeyJWT)
                .compact();
    }

    public Optional<Long> getUserId(String token) {

        try {
            Claims claims = parse(token).getBody();
            String subject = claims.getSubject();

            return Optional.ofNullable(subject)
                    .map(Long::parseLong);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(privateKeyJWT).parseClaimsJws(token);
    }
}


