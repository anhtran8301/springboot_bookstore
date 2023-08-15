package com.springboot.blog.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}
