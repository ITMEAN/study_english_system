package org.minh.identityservice.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.minh.identityservice.model.response.LoginResponse;
import org.minh.identityservice.service.auth.AuthService;
import org.minh.identityservice.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandle implements AuthenticationSuccessHandler {
    @Autowired
    private  AuthService authService;
    @Value("${url.frontend}")
    private String frontend;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.err.println("authentication"+authentication);
        try {
            LoginResponse loginResponse = authService.OAuth2Login((OAuth2AuthenticationToken) authentication);
            String redirectUrl =
                    String.format(frontend+"/?token=%s&refreshToken=%s&tokenType=%s&roles=%s&email=%s&fullName=%s",
                            loginResponse.getToken(),
                            loginResponse.getRefreshToken(),
                            loginResponse.getTokenType(),
                            loginResponse.getRoles(),
                            loginResponse.getEmail(),
                            loginResponse.getFullName()
                    );
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
