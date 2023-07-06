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
    public String orderInfo;
    public long amount;
    public String partnerName;
    public String subPartnerCode;
    public String requestType;
    public String redirectUrl;
    public String ipnUrl;
    public String storeId;
    public String extraData;
    public String partnerClientId;
    public Boolean autoCapture = true;
    public String signature;
    public String partnerCode;
    public String orderId;
    public String requestId;
    public String lang;
}
