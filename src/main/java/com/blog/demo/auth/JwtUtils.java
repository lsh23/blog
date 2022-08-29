package com.blog.demo.auth;

import com.blog.demo.exception.InvalidTokenException;
import com.blog.demo.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    private final String secretKey;
    private final long validityInMilliseconds;
    private final JwtParser jwtParser;

    public JwtUtils(@Value("${spring.jwt.token.secret-key}") String secretKey,
                    @Value("${spring.jwt.token.expire-length}") long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createToken(Map<String, Object> payload) {
        Claims claims = Jwts.claims(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public String getPayload(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public static PayloadBuilder payloadBuilder() {
        return new PayloadBuilder();
    }

    public static class PayloadBuilder {
        private final Claims claims;
        private final String USER_EMAIL = "email";
        private final String USER_LOGIN_ID = "loginId";
        private final String USER_ID = "userId";

        private PayloadBuilder() {
            this.claims = Jwts.claims();
        }

        public PayloadBuilder setUserEmail(String email) {
            claims.put(USER_EMAIL,email);
            return this;
        }

        public PayloadBuilder setUserId(Long userId) {
            claims.put(USER_ID,userId);
            return this;
        }

        public PayloadBuilder setUserLoginId(String userLoginId) {
            claims.put(USER_LOGIN_ID,userLoginId);
            return this;
        }

        public Map<String, Object> build() {
            return claims;
        }
    }

}
