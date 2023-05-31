package com.springboot.blog.controller;

import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Login and Register APIs"
)
public class AuthController {
    private AuthService authService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //build login REST API
    @Operation(
            summary = "Login/Sign-in API",
            description = "API for user login/sign-in"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 Login Successfully"
    )
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    //build register REST API
    @Operation(
            summary = "Register/Sign-up API",
            description = "API for user registration"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 User Registered Successfully"
    )
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return authService.logout();
    }

}

