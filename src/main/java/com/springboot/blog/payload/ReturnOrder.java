package com.springboot.blog.payload;

import com.springboot.blog.entity.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnOrder {
    private Order order;
}
