package org.minh.identityservice.service.auth;

import org.minh.identityservice.entity.Users;
import org.minh.identityservice.enums.StatusOTP;
import org.minh.identityservice.model.request.LoginRequest;
import org.minh.identityservice.model.request.ResetPasswordRequest;
import org.minh.identityservice.model.response.LoginResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface AuthService {
    String authenticate(LoginRequest request) throws Exception ;
    Users getUserDetailsFromToken(String token) throws Exception;
    Users getUserDetailsFromRefreshToken(String token) throws Exception;
    StatusOTP validateOTPRegister(String email, String otp) throws Exception;
    void forgotPassword(String email) throws Exception;
    void resetPassword(ResetPasswordRequest request) throws Exception;
    LoginResponse OAuth2Login(OAuth2AuthenticationToken authentication) throws Exception;

}
