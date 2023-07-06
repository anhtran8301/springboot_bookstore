package com.springboot.blog.payload;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    private Order order;
    private Payment payment;

}
