package com.springboot.blog.entity.composite;

import com.springboot.blog.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderPKId implements Serializable {
    private Long transId;
    private Order order;
}
