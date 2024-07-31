package org.minh.identityservice.service.email;

import org.minh.identityservice.enums.StatusOTP;

public interface EmailService {
    boolean sendOTPRegister (String email);
    StatusOTP validateOTP (String otp, String email);
    void sendForgotPassword(String email,String token);
}
