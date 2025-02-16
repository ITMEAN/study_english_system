package org.minh.identityservice.service.token;

import lombok.RequiredArgsConstructor;
import org.minh.identityservice.entity.Token;
import org.minh.identityservice.entity.Users;
import org.minh.identityservice.exception.DataNotFoundException;
import org.minh.identityservice.exception.ExpiredTokenException;
import org.minh.identityservice.jwt.JwtUtil;
import org.minh.identityservice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private int expiratuon;
    @Value("${jwt.expiration-refresh}")
    private int expirationRefreshToken;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Token addToken(Users user, String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUsers(user);
        int tokenCount = userTokens.size();
        // Số lượng token vượt quá giới hạn, xóa một token cũ
        if (tokenCount >= MAX_TOKENS) {
            //kiểm tra xem trong danh sách userTokens có tồn tại ít nhất
            //một token không phải là thiết bị di động (non-mobile)
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                //tất cả các token đều là thiết bị di động,
                //chúng ta sẽ xóa token đầu tiên trong danh sách
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        long expirationInSeconds = expiratuon;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        // Tạo mới một token cho người dùng
        Token newToken = Token.builder()
                .users(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .experiationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();

        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public Token refreshToken(String refreshToken, Users user) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if(existingToken == null) {
            throw new DataNotFoundException("Refresh token does not exist");
        }
        if(existingToken.getExperiationDate().compareTo(LocalDateTime.now()) < 0){
            tokenRepository.delete(existingToken);
            throw new ExpiredTokenException("Refresh token is expired");
        }
        String token = jwtUtil.generateToken(user);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiratuon);
        existingToken.setExperiationDate(expirationDateTime);
        existingToken.setToken(token);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }
}
