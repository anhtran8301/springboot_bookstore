package com.springboot.blog.momo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.Transaction;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.momo.config.MomoConfig;
import com.springboot.blog.momo.payload.NotificationRequest;
import com.springboot.blog.momo.payload.PaymentRequest;
import com.springboot.blog.momo.payload.PaymentResponse;
import com.springboot.blog.momo.service.MomoService;
import com.springboot.blog.momo.utils.MomoEncoderUtils;
import com.springboot.blog.repository.OrderRepository;
import com.springboot.blog.repository.TransactionRepository;
import com.springboot.blog.service.OrderService;
import com.springboot.blog.utils.AppConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MomoServiceImpl implements MomoService {


    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final RestTemplate restTemplate;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public String createRequest(Long userId, Order order, List<Long> checkedCartId, String payType) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (!order.getUser().getId().equals(userId)) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "User Id not valid");
        }

        if (order.isPay()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error is pay");
        }

        var accessKey = MomoConfig.ACCESS_KEY;
        var secretKey = MomoConfig.SECRET_KEY;

        NotificationRequest request = new NotificationRequest();
        request.orderInfo = MomoConfig.ORDER_INFO;
        request.partnerCode = MomoConfig.PARTNER_CODE;
        request.ipnUrl = MomoConfig.IPN_URL;
        request.redirectUrl = MomoConfig.REDIRECT_URL;
        request.amount = (long) order.getTotalPrice() * 2360;
        request.orderId = String.valueOf(order.getId());
        request.requestId = String.valueOf(order.getId());
        request.requestType = payType;
        request.extraData = "";
        request.partnerName = MomoConfig.PARTNER_NAME;
        request.storeId = MomoConfig.STORE_ID;
        request.lang = MomoConfig.LANGUAGE;
        if (isExpiredOrderTransaction(order)) {
            System.out.println("!!!!!!!");
            request.requestId = order.getId() + "REQ" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        }

        if (checkedCartId != null) {
            orderService.createOrder(order, checkedCartId);
        }

        var rawSignature =
                        "accessKey=" + accessKey +
                        "&amount=" + request.amount +
                        "&extraData=" + request.extraData +
                        "&ipnUrl=" + request.ipnUrl +
                        "&orderId=" + request.orderId +
                        "&orderInfo=" + request.orderInfo +
                        "&partnerCode=" + request.partnerCode +
                        "&redirectUrl=" + request.redirectUrl +
                        "&requestId=" + request.requestId +
                        "&requestType=" + request.requestType;

        request.signature = MomoEncoderUtils.signHmacSHA256(rawSignature, secretKey);
        System.out.println(request.signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> quickPayResponse = restTemplate.exchange(MomoConfig.CREATE_URL, HttpMethod.POST, httpEntity, String.class);
        String contents = quickPayResponse.getBody();
        PaymentResponse paymentRes = new ObjectMapper().readValue(contents, PaymentResponse.class);

        if (quickPayResponse.getStatusCode().isError()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error occurred during payment.");
        }

        if (!(paymentRes.payUrl).isEmpty()) {
            return paymentRes.payUrl;
        } else {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Payment URL is empty.");
        }
    }

    @Override
    public void handleResponse(PaymentRequest request) {

        if (request.resultCode == MomoConfig.SUCCESS_RESULT_CODE) {
            var order = orderRepository.findById(Long.valueOf(request.orderId)).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "id", Long.parseLong(request.orderId)));
            order.setPay(true);

            order.setStatus(AppConstants.IS_PAYMENT);
        } else throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Fail");

        var order = orderRepository.findById(Long.valueOf(request.orderId)).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", Long.parseLong(request.orderId)));

        var transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setRequestId(request.requestId);
        transaction.setAmount(request.amount);
        transaction.setPartnerUserId(MomoConfig.PARTNER_CODE);
        transaction.setOrderType(request.orderType);
        transaction.setTransId(request.transId);
        transaction.setResultCode(request.resultCode);
        transaction.setPayType(request.payType);
        transaction.setResponseTime(changeTimeStampToDateTime(request.responseTime));
        transaction.setSignature(request.signature);
//        transaction.setOrder(null);
        System.out.println(transaction);
        transactionRepository.save(transaction);
    }


    private boolean isExpiredOrderTransaction(Order order) {
        var timeExpiredAfterFirstTransaction = order.getDateCreated().plusHours(1).plusMinutes(40);

        return timeExpiredAfterFirstTransaction.isBefore(LocalDateTime.now());
    }

    public LocalDateTime changeTimeStampToDateTime(long original) {
        Instant instant = Instant.ofEpochMilli(original);
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
