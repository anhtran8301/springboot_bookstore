package com.springboot.blog.momo.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequest {
    public String partnerCode;
    public String orderId;
    public String requestId;
    public long amount;
    public String orderInfo;
    public String partnerUserId;
    public String orderType;
    public long transId;
    public int resultCode;
    public String message;
    public String payType;
    public long responseTime;
    public String extraData;
    public String signature;
}
