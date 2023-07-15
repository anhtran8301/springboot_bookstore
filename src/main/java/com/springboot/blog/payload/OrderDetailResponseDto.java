package com.springboot.blog.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponseDto {
    private Long id;
    private Long bookId;
    private String name;
    private Long orderId;
    private int quantity;
    private double price;

}
