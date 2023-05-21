package com.springboot.blog.payload;

import com.springboot.blog.security.UserPrinciple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {

    private String accessToken;
    private String tokenType;

    private String name;
    private Collection<? extends GrantedAuthority> roles;

}
