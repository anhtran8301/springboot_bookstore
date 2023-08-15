package com.springboot.blog.momo.payload;

import java.util.List;

public class NotificationRequest {
    public String partnerCode;
    public String redirectUrl;
    public String ipnUrl;
    public long amount;

    public String orderId;
    public String orderInfo;
    public String requestId;
    public String extraData;
    public String partnerName;
    public String storeId;
    public String requestType;
    public String orderGroupId;
    public boolean autoCapture;
    public String lang;
    public String signature;
//    public List<Item> items;
//    public DeliveryInfo deliveryInfo;
//    public UserInfo userInfo;
}
