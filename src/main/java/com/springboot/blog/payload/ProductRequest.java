package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ProductRequest {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String sku;
    private String name;
    private String description;
    private boolean active;
    private String imagesString;
    private String slug;
    private double unitPrice;
    private int sold;
    private String cover;
    private int discount;
    private Date releaseDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateCreated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateUpdated;

    private Long bookCategoryId;
    private Long authorId;
    private Long publisherId;
}
