package com.springboot.blog.payload;

import com.springboot.blog.security.UserPrinciple;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserPrinciple data;
}
