package com.springboot.blog.service;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.OrderRequestDto;
import com.springboot.blog.payload.OrderResponseDto;

import java.util.List;

public interface OrderService {
//    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);

    Order checkOut (User user, OrderRequestDto orderRequestDto);

    void createOrder(Order order);

    List<OrderResponseDto> getOrders(User user);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto getOrderById(Long id);

}
