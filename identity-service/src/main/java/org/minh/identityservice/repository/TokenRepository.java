package org.minh.identityservice.repository;

import org.minh.identityservice.entity.Token;
import org.minh.identityservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByRefreshToken(String refreshToken);
    Token findByToken(String token);
    List<Token> findByUsers(Users users);
}