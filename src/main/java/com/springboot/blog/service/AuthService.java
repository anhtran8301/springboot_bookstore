package com.springboot.blog.service;

import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.security.UserPrinciple;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);

//    UserPrinciple getPrincipal();

    String register(RegisterDto registerDto);
}
