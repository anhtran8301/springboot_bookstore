package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.ChangeAvatar;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.PasswordResetRequest;
import com.springboot.blog.payload.RegisterDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface AuthService {
    ResponseEntity<?> login(LoginDto loginDto);

    ResponseEntity<?> register(RegisterDto registerDto);

    ResponseEntity<?> refreshToken(HttpServletRequest request);

    ResponseEntity<?> logout();

    String resetPasswordRequest(PasswordResetRequest passwordResetRequest,
                                HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException;

    String resetPassword(PasswordResetRequest passwordResetRequest, String token);

//    ResponseEntity<?> changeAvatar(HttpServletRequest httpServletRequest, @Valid @RequestBody ChangeAvatar changeAvatar);
    ResponseEntity<?> changeAvatar(HttpServletRequest httpServletRequest, ChangeAvatar changeAvatar);

}
