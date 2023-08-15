package com.springboot.blog.payload.SaleDots;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleProductReq {
    private Long productId;
    private int discount;
    private int quantity;
}
