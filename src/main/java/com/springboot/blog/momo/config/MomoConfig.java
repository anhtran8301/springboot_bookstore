package com.springboot.blog.momo.config;

public class MomoConfig {
    public static String ACCESS_KEY = "F8BBA842ECF85";
    public static String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    public static String ORDER_INFO = "Pay with momo";
    public static String PARTNER_CODE = "MOMO";
    public static String IPN_URL = "https://katitvn.id.vn/api/v1/orders/momo-payment-request";
    public static String REDIRECT_URL = "https://katitvn.id.vn/api/v1/orders/success";
    public static String REQUEST_TYPE = "payWithMethod";
    public static String PARTNER_NAME = "MoMo Payment";
    public static String STORE_ID = "BlogApp";
    public static boolean AUTO_CAPTURE = true;
    public static String LANGUAGE = "vi";
    public static String CREATE_URL = "https://test-payment.momo.vn/v2/gateway/api/create";
    public static int SUCCESS_RESULT_CODE = 0;
//    public static String CREATE_ORDER_URL = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
//    public static String PAY_QUERY_STATUS_URL = "https://test-payment.momo.vn/pay/query-status";
//    public static String PAY_CONFIRM_URL = "https://test-payment.momo.vn/pay/confirm";
//    public static String RETURN_URL = "http://localhost:8080/api/momo/test";
//    public static String NOTIFY_URL = "http://localhost:3000/success/payment";

}
