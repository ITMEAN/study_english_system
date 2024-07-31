package org.minh.gatewayservice.service.token;


import org.minh.gatewayservice.entity.Token;
import org.minh.gatewayservice.entity.Users;

public interface TokenService {
    Token addToken(Users user, String token, boolean isMobileDevice);

    Token refreshToken(String refreshToken, Users user) throws Exception;
}
