package org.minh.gatewayservice.repository;



import org.minh.gatewayservice.entity.Token;
import org.minh.gatewayservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByRefreshToken(String refreshToken);
    Token findByToken(String token);
    List<Token> findByUsers(Users users);
}