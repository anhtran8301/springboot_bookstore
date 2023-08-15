package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ProductDto {

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
    private int quantity;
    private Date releaseDate;
    private Date saleStartDate;
    private Date saleEndDate;
    private int saleQuantity;
    private int saleSold;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateCreated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateUpdated;

    private Long bookCategoryId;
    private Long authorId;
    private Long publisherId;

}
