package org.minh.identityservice.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.minh.identityservice.entity.Users;
import org.minh.identityservice.enums.StatusOTP;
import org.minh.identityservice.exception.DataNotFoundException;
import org.minh.identityservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService{
    @Autowired
    private final JavaMailSender javaMailSender;
    @Autowired
    private UsersRepository usersRepository;
    @Value("${url.frontend}")
    private String url;

    @Override
    public boolean sendOTPRegister(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(()->new DataNotFoundException("Cannot find email"));
        String otp = generateOTP();
        try{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long updateAt = timestamp.getTime();
            user.setOtp(otp);
            user.setUpdateAt(updateAt);
            usersRepository.save(user);
            // create template
            String htmlContent = loadHtmlTemplate("templates/OTP.html");
            htmlContent = htmlContent.replace("codeOtp", otp);
            htmlContent = htmlContent.replace("userName", email);
            sendHtmlEmail(email,"OTP xác thực đăng ký", htmlContent);
            return true;
        }catch (Exception e){
            throw new RuntimeException("Error while sending OTP register"+e.getMessage());
        }
    }

    @Override
    public StatusOTP validateOTP(String otp, String email) {
        Users users = usersRepository.findByEmail(email).orElseThrow(()->new DataNotFoundException("Cannot find email"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long now = timestamp.getTime();
        long startTime = users.getUpdateAt();
        long diff = now - startTime;
        if(diff > 60000){
            return StatusOTP.EXPIRED;
        }else {
            return (otp.equals(users.getOtp()) ? StatusOTP.VALID : StatusOTP.INVALID);
        }
    }

    @Override
    public void sendForgotPassword(String email,String token) {
        Users user = usersRepository.findByEmail(email).orElseThrow(()->new DataNotFoundException("Cannot find email"));
        try{
            // create template
            String urlResetPassword = url + "/reset-password?token=" + token+"&email="+email;
            String htmlContent = loadHtmlTemplate("templates/OTP.html");
            htmlContent = htmlContent.replace("Do not share this code with others", urlResetPassword);
            htmlContent = htmlContent.replace("userName", email);
            sendHtmlEmail(email,"Tạo lại mật khẩu", htmlContent);
        }catch (Exception e){
            throw new RuntimeException("Error while sending OTP forgot password"+e.getMessage());
        }
    }

    private static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            otp.append(random.nextInt(10)); // Generate a random number between 0-9
        }
        return otp.toString();
    }

    private String loadHtmlTemplate(String templateName) throws IOException {
        try(InputStream inputStream = new ClassPathResource(templateName).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw e;
        }
    }
}
