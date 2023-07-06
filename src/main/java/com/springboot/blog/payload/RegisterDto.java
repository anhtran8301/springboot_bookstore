package com.springboot.blog.payload;

import com.springboot.blog.utils.AppConstants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String username;

    @Email
    @NotNull
    @NotEmpty
    @NotBlank
    private String email;

    private String avatar;

    @NotNull
    @NotEmpty
    @NotBlank
    @Pattern(regexp = AppConstants.REGEX_PASSWORD,
            message = AppConstants.REGEX_PASSWORD_MESSAGE)
    private String password;

}
