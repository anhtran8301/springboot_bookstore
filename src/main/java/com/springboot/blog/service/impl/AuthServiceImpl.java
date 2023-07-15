package com.springboot.blog.service.impl;

import com.springboot.blog.entity.RefreshToken;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.TokenRefreshException;
import com.springboot.blog.mail.EmailUtils;
import com.springboot.blog.payload.*;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtAuthenticationFilter;
import com.springboot.blog.security.JwtUtils;
import com.springboot.blog.security.RefreshTokenService;
import com.springboot.blog.security.UserPrinciple;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.service.PasswordResetTokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final EmailUtils emailUtil;
    private final PasswordResetTokenService passwordResetTokenService;
    private final JwtAuthenticationFilter jwtTokenFilter;

    @Value("${server.url}")
    String serverUrl;

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
       try {
           Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   loginDto.getUsernameOrEmail(), loginDto.getPassword()));

           SecurityContextHolder.getContext().setAuthentication(authentication);

           UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
           ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userPrinciple);
           RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrinciple.getId());
           ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

           return ResponseEntity.ok()
                   .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                   .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                   .body(new JWTAuthResponse(
                           jwtCookie.getValue(),
                           jwtRefreshCookie.getValue(),
                           userPrinciple)
);


       } catch (AuthenticationException e) {
           throw new BlogAPIException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password supplied");
       }
    }

    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        if(
                registerDto.getUsername() == null ||
                        registerDto.getEmail() == null ||
                        registerDto.getName() == null ||
                        registerDto.getPassword() == null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Please complete all information");
        }

        //add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists");
        }

        //add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }
        //if avatar == null, set default avatar
        if(registerDto.getAvatar() == null || registerDto.getAvatar().trim().isEmpty()){
            registerDto.setAvatar("https://firebasestorage.googleapis.com/v0/b/srping-boot-blog.appspot.com/o/images%2Fuser-image.jpg?alt=media&token=b0b9d5b9-5a44-4468-9f38-b0b256c12cb5");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setAvatar(registerDto.getAvatar());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully"), HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not exists!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }

    @Override
    public ResponseEntity<?> logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principle.toString().equals("anonymousUser")) {
            Long userId = ((UserPrinciple) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
    @Override
    public String resetPasswordRequest(PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest servletRequest) throws MessagingException, UnsupportedEncodingException {

        Optional<User> user = userRepository.findByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        if (user.isPresent()) {
            String passwordResetToken = UUID.randomUUID().toString();
            createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), serverUrl, passwordResetToken);
        } else {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid email. Please check your email again");
        }
        return passwordResetUrl;
    }

    @Override
    public String resetPassword(PasswordResetRequest passwordResetRequest, String token) {
        String tokenVerificationResult = validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid token password reset token");
//            return "Invalid token password reset token";
        }
        Optional<User> user = Optional.ofNullable(findUserByPasswordToken(token));
        if (user.isPresent()) {
            resetPassword(user.get(), passwordResetRequest.getNewPassword());
            return "Password has been reset successfully";
        }
//        return "Invalid password reset token";
        throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid password reset token");
    }

    @Override
    public ResponseEntity<?> changeAvatar(HttpServletRequest httpServletRequest, ChangeAvatar changeAvatar){
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user;
        try {
            if(changeAvatar.getAvatar() == null || changeAvatar.getAvatar().trim().equals("")){
                return new ResponseEntity<>(new MessageResponse("Fail"), HttpStatus.BAD_REQUEST);
            } else{
                user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with -> username: " + username));
                user.setAvatar(changeAvatar.getAvatar());
                userRepository.save(user);
            }
            return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordToken)
            throws MessagingException, UnsupportedEncodingException {

        String url = MessageFormat.format("{0}/api/v1/auth/reset-password?token={1}",applicationUrl, passwordToken);
        emailUtil.sendPasswordResetVerificationEmail(user, passwordToken, applicationUrl);
        log.info("Click the link to reset your password :  {}", url);
        return url;
    }

//    private String applicationUrl(HttpServletRequest request) {
//        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//    }

    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
    }

    public String validatePasswordResetToken(String token) {
        return passwordResetTokenService.validatePasswordResetToken(token);
    }

    public User findUserByPasswordToken(String token) {
        return passwordResetTokenService.findUserByPasswordToken(token).get();
    }

    public void resetPassword(User theUser, String newPassword) {
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);
    }

//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }


}
