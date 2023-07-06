package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCategoryDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;
    private String image;
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;
}
