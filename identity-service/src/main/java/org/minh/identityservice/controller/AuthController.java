package org.minh.identityservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.minh.identityservice.entity.Token;
import org.minh.identityservice.entity.Users;
import org.minh.identityservice.enums.StatusOTP;
import org.minh.identityservice.exception.BadCredentialsException;
import org.minh.identityservice.jwt.JwtUtil;
import org.minh.identityservice.model.request.*;
import org.minh.identityservice.model.response.LoginResponse;
import org.minh.identityservice.service.auth.AuthService;
import org.minh.identityservice.service.email.EmailService;
import org.minh.identityservice.service.token.TokenService;
import org.minh.identityservice.service.user.UserService;
import org.minh.identityservice.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final AuthService authService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws Exception {
//        return new ResponseEntity<>(authService.authenticate(loginRequest), HttpStatus.ACCEPTED);
        String token = authService.authenticate(loginRequest);
        String userAgent = httpServletRequest.getHeader("User-Agent");
        Users userDetail = authService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token, isMobileDevice(userAgent));
        LoginResponse loginResponse = LoginResponse.builder()
                .message("Login Successful!")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .email(userDetail.getEmail())
                .fullName(userDetail.getFullName())
                .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) throws Exception {
        Users userDetail = authService.getUserDetailsFromRefreshToken(request.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(request.getRefreshToken(), userDetail);
        LoginResponse loginResponse = LoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .email(userDetail.getEmail())
                .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .fullName(userDetail.getFullName())
                .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) throws Exception {
        authService.forgotPassword(email);
        return new ResponseEntity<>("Link reset password send to your email", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) throws Exception {
        authService.resetPassword(request);
        return new ResponseEntity<>("Reset password successful", HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest user) {
        Users users = userService.createUser(user);
        if (users != null) {
            return emailService.sendOTPRegister(user.getEmail()) ?
                    new ResponseEntity<>(user, HttpStatus.OK) :
                    new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Something went wrong when register", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate-otp-register")
    public ResponseEntity<?> validateOTPRegister(@RequestBody @Valid ValidOtpRequest request) throws Exception {
        StatusOTP otp = authService.validateOTPRegister(request.getEmail(), request.getOtp());
        return switch (otp) {
            case VALID -> new ResponseEntity<>("create account successful", HttpStatus.OK);
            case INVALID -> new ResponseEntity<>("Invalid", HttpStatus.BAD_REQUEST);
            case EXPIRED -> new ResponseEntity<>("expired", HttpStatus.BAD_REQUEST);
        };
    }

    @GetMapping("/resend-otp/{email}")
    public ResponseEntity<?> resendOTP(@PathVariable("email") String email) throws Exception {
        System.out.println(email);
        return emailService.sendOTPRegister(email) ?
                new ResponseEntity<>("OTP sent successful", HttpStatus.OK) :
                new ResponseEntity<>("OTP could not be sent", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    private boolean isMobileDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }

    @PostMapping("/users/is-exist")
    public ResponseEntity<?> isExistUser(@RequestBody String email, @RequestHeader("Authorization") String token) {
        System.out.println(token);
        return ResponseEntity.ok(userServiceImpl.isUserExist(email));
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email, @RequestHeader("Authorization") String token) throws BadCredentialsException {
        System.err.println("email = " + email);
        return ResponseEntity.ok(userService.getUserByEmail(email,token));
    }

}
