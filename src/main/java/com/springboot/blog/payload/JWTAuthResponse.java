package com.springboot.blog.payload;

import com.springboot.blog.security.UserPrinciple;
import com.springboot.blog.service.AuthService;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {

    private String accessToken;
//    private String tokenType;

     private UserPrinciple userData;


}
