package com.springboot.blog.controller;

import com.springboot.blog.payload.ChangeAvatar;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.PasswordResetRequest;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {


        return authService.register(registerDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return authService.logout();
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<String> resetPasswordRequest(@Valid @RequestBody PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest servletRequest) throws MessagingException, UnsupportedEncodingException {
        String url = authService.resetPasswordRequest(passwordResetRequest, servletRequest);

        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String token) {

        return new ResponseEntity<>(authService.resetPassword(passwordResetRequest, token), HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(HttpServletRequest httpServletRequest, @Valid @RequestBody ChangeAvatar changeAvatar){
        return authService.changeAvatar(httpServletRequest, changeAvatar);
    }
}

