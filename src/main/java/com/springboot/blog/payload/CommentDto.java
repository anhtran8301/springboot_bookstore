package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty()
    @Email
    private String email;

    @NotEmpty()
    @Size(min = 10, message = "Comment body should have at least 10 characters")
    private String body;
}
