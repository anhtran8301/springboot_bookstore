package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthService {
    ResponseEntity<?> login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    ResponseEntity<?> refreshToken(HttpServletRequest request);

    ResponseEntity<?> logout();

}
