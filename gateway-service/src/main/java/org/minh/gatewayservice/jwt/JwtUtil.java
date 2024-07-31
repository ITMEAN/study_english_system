package org.minh.gatewayservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.minh.gatewayservice.entity.Token;
import org.minh.gatewayservice.entity.Users;
import org.minh.gatewayservice.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Autowired
    private final TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    public String generateToken(Users user) throws Exception {
        //properties => claims
        Map<String, Object> claims = new HashMap<>();
        // Add subject identifier (phone number or email)
        String subject = getSubject(user);
        claims.put("subject", subject);
        // Add user ID
        claims.put("userId", user.getEmail());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) //how to extract claims from this ?
                    .setSubject(subject)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            //you can "inject" Logger, instead System.out.println
            throw new InvalidParameterException("Cannot create jwt token, error: " + e.getMessage());
            //return null;
        }
    }


    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private static String getSubject(Users user) {
        // Determine subject identifier (phone number or email)
        String subject = user.getEmail();
        if (subject == null || subject.isBlank()) {
            // If phone number is null or blank, use email as subject
            subject = user.getEmail();
        }
        return subject;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignInKey())
                .build().parseClaimsJws(token)
                .getBody();
    }


    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractClaims(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String getSubject(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean validateToken(String token, Users userDetails) {
        try {
            String subject = extractClaims(token, Claims::getSubject);
            //subject is phoneNumber or email
            Token existingToken = tokenRepository.findByToken(token);
            if (existingToken == null ||
                    existingToken.isRevoked() == true ||
                    !userDetails.isActive()
            ) {
                return false;
            }
            return (subject.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
