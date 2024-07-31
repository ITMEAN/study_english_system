package org.minh.identityservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.minh.identityservice.entity.Role;
import org.minh.identityservice.entity.Token;
import org.minh.identityservice.entity.Users;
import org.minh.identityservice.enums.StatusOTP;
import org.minh.identityservice.exception.BadCredentialsException;
import org.minh.identityservice.exception.DataNotFoundException;
import org.minh.identityservice.exception.ExpiredTokenException;
import org.minh.identityservice.jwt.JwtUtil;
import org.minh.identityservice.model.request.LoginRequest;
import org.minh.identityservice.model.request.ResetPasswordRequest;
import org.minh.identityservice.model.response.LoginResponse;
import org.minh.identityservice.repository.RoleRepository;
import org.minh.identityservice.repository.TokenRepository;
import org.minh.identityservice.repository.UsersRepository;
import org.minh.identityservice.service.email.EmailService;
import org.minh.identityservice.service.token.TokenService;
import org.minh.identityservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final TokenService tokenService;

    @Override
    public String authenticate(LoginRequest request) throws Exception {
        Users users = null;
        String subject = null;
        // check if the user exists by email
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            users = userService.getUserById(request.getEmail());
            subject = request.getEmail();
        }
        //
        if (users == null) {
            throw new DataNotFoundException("cannot find user");
        }
        // check password
        if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
            throw new BadCredentialsException("Email or password is incorrect");
        }

        if (!users.isActive()) {
            throw new DataNotFoundException("User is not active");
        }


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(subject, request.getPassword(), users.getAuthorities());

        //authenticate with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtUtil.generateToken(users);

    }

    @Override
    public Users getUserDetailsFromToken(String token) throws Exception {
        if (jwtUtil.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String subject = jwtUtil.getSubject(token);
        return userService.getUserById(subject);
    }

    @Override
    public Users getUserDetailsFromRefreshToken(String token) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(token);
        return getUserDetailsFromToken(existingToken.getToken());
    }


    @Override
    public StatusOTP validateOTPRegister(String email, String otp) throws Exception {
        StatusOTP statusOTP = emailService.validateOTP(otp, email);
        switch (statusOTP) {
            case VALID -> {
                Users user = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("cannot find user by email:" + email));
                user.setActive(true);
                usersRepository.save(user);
            }
        }
        return statusOTP;
    }


    @Override
    public void forgotPassword(String email) throws Exception {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("cannot find user by email:" + email));
        String token = jwtUtil.generateToken(user);
        emailService.sendForgotPassword(email, token);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) throws Exception {
        Users user = usersRepository.findByEmail(request.getEmail()).orElseThrow(() -> new DataNotFoundException("cannot find user by email:" + request.getEmail()));
        if (!request.getRePassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (jwtUtil.isTokenExpired(request.getToken())) {
            throw new ExpiredTokenException("Token is expired");
        }
        if (!jwtUtil.getSubject(request.getToken()).equals(request.getEmail())) {
            throw new BadCredentialsException("Token is invalid");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        usersRepository.save(user);


    }

    @Override
    public LoginResponse OAuth2Login(OAuth2AuthenticationToken authentication) throws Exception {
        try{
            String email = authentication.getPrincipal().getAttribute("email");
            Users user = usersRepository.findByEmail(email).orElse(null);
            if(user == null){
                Role role = roleRepository.findById(2).orElseThrow(() -> new DataNotFoundException("Role not found"));
                user = Users.builder().fullName(authentication.getPrincipal().getAttribute("name"))
                        .email(email)
                        .active(true)
                        .role(role)
                        .build();
                usersRepository.save(user);

            }
            String token = jwtUtil.generateToken(user);
            Token refreshToken = tokenService.addToken(user,token,false);
            return LoginResponse.builder()
                    .message("Login with OAuth2 successfully")
                    .token(token)
                    .tokenType("Bearer")
                    .refreshToken(refreshToken.getRefreshToken())
                    .roles(user.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .build();


        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new Exception("Cannot login with OAuth2");
        }

    }


}
