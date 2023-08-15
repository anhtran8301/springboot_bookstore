package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private int quantity;

    private Long productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;
//    private int discount;

}
