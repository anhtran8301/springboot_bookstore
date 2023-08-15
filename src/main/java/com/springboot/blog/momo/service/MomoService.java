package com.springboot.blog.momo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.blog.entity.Order;
import com.springboot.blog.momo.payload.PaymentRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MomoService {
    String createRequest(Long userId, Order order, List<Long> checkedCartId, String payType) throws IOException, NoSuchAlgorithmException, InvalidKeyException;

    void handleResponse(PaymentRequest request);
}
