package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.Payment;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.OrderRequestDto;
import com.springboot.blog.payload.OrderResponseDto;
import com.springboot.blog.repository.OrderRepository;
import com.springboot.blog.repository.PaymentRepository;
import com.springboot.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        Order order = orderRequestDto.getOrder();
        order.setStatus("INPROGRESS");
        order.setOrderTackingNumber(UUID.randomUUID().toString());
        orderRepository.save(order);

        Payment payment = orderRequestDto.getPayment();

        if(!payment.getType().equals("DEBIT")) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Payment card type do not support");
        }
        payment.setOrderId(order.getId());
        paymentRepository.save(payment);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderTackingNumber(order.getOrderTackingNumber());
        orderResponseDto.setStatus(order.getStatus());
        orderResponseDto.setMessage("SUCCESS");

        return orderResponseDto;
    }
}
