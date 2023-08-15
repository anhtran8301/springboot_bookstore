package com.springboot.blog.payload.SaleDots;

import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.ProductResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleProductRes {
    private Long productId;
    private int discount;
    private int quantity;
    private ProductDto product;
}
