package com.springboot.blog.payload;

import com.springboot.blog.utils.AppConstants;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @Email
    private String email;

    @Pattern(regexp = AppConstants.REGEX_PASSWORD,
            message = AppConstants.REGEX_PASSWORD_MESSAGE)
    private String newPassword;
}
