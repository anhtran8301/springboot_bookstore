package com.springboot.blog.payload;

import com.springboot.blog.entity.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private String status;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
    private Long userId;
    private String shipName;
    private String shipNote;
    private String shipPhone;
    private String displayAddress;
    private double totalPrice;
    private String payType;
    private boolean isPay;
    private LocalDateTime dateOfPayment;

    private List<OrderDetailResponseDto> orderDetails;
}



