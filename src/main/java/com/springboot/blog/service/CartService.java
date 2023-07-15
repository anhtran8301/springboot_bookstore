package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.CartDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CartService {
    CartDto addToCart(User user, Long productId, int quantity);
    List<CartDto> getCarts(Long userId);
    void clearCart(Long userId);
    void removeFromCart(Long userId, Long productId);
    List<CartDto> addBatchCart(User user, List<CartDto> listCart);

    void removeBatchCart(List<Long> cartIds);
}


