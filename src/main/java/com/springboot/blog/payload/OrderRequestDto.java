package com.springboot.blog.payload;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private List<Long> checkedCartId;
    private String shipNote;
    private Long userAddressId;
    private String payType;
    private BigDecimal DeliveryFee;
}
