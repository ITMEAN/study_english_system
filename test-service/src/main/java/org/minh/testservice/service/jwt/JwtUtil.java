package org.minh.testservice.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;


    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
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


    public String getSubject(String token) {
        return extractClaims(token, Claims::getSubject);
    }


}
