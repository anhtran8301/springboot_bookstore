package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    private String sku;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private boolean active;

    @NotEmpty
    private String imageUrl;

    @NotEmpty
    private double unitPrice;

    @NotEmpty
    private int unitsInStock;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateCreated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateUpdated;

    private Long bookCategoryId;
}
