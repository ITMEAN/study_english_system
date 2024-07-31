package org.minh.identityservice.service.token;

import org.minh.identityservice.entity.Token;
import org.minh.identityservice.entity.Users;

public interface TokenService {
    Token addToken(Users user, String token, boolean isMobileDevice);
    Token refreshToken(String refreshToken, Users user) throws Exception;
}
