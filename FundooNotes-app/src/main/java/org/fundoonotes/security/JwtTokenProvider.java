package org.fundoonotes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

    @Component
    public class JwtTokenProvider {

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Value("${jwt.expiration}")
        private long jwtExpiration;

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }

        public String generateToken(String email) {

            Date currentDate = new Date();

            Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

            return Jwts.builder()
                    .subject(email)
                    .issuedAt(currentDate)
                    .expiration(expireDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();

        }
        public String getEmail(String token){

            Claims claims = Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();

        }

        public boolean validateToken(String token){

            try{
                Jwts.parser()
                        .verifyWith((javax.crypto.SecretKey)getSigningKey())
                        .build()
                        .parseSignedClaims(token);

                return true;

            }
            catch (Exception e){
                return false;
            }

        }

    }
