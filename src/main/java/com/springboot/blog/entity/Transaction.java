package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.blog.entity.composite.OrderPKId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(OrderPKId.class)
public class Transaction {
    @Id
    private Long transId;

    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;


    private String requestId;
    private BigDecimal amount;
    private String partnerUserId;
    private String orderType;

    private int resultCode;
    private String payType;
    private LocalDateTime responseTime;
    private String signature;


}
