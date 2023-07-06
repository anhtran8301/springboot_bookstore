package com.springboot.blog.momo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


import com.springboot.blog.momo.config.MomoConfig;
import com.springboot.blog.momo.payload.PaymentRequest;
import com.springboot.blog.momo.utils.MomoEncoderUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/momo")
@Tag(
        name = "Momo Dev APIs Resource"
)
public class MomoController {
    private static final Logger logger = LoggerFactory.getLogger(MomoController.class);
    @GetMapping(value = "/test")
    public String test() {
        return "Test";
    }

    // tạo thanh toán, response trả về pay url
    @GetMapping(value = "/create-order")
    public Map<String, Object> createPayment() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
//        JSONObject json = new JSONObject();
        String partnerCode = MomoConfig.PARTNER_CODE;
        String accessKey = MomoConfig.ACCESS_KEY;
        String secretKey = MomoConfig.SECRET_KEY;
        String returnUrl = MomoConfig.REDIRECT_URL;
        String notifyUrl = MomoConfig.NOTIFY_URL;

        PaymentRequest request = new PaymentRequest();
        request.orderId = "Tuananh12345551235";
        request.orderInfo = "orderInfo" + "123123";
        request.partnerCode = partnerCode;
        request.redirectUrl = "";
        request.ipnUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
        request.amount = 10000;

        request.requestId = String.valueOf(System.currentTimeMillis());
        request.extraData = "";
        request.partnerName = "MoMo Payment";
        request.storeId = "Test Store";
        request.autoCapture = true;
        request.lang = "vi";
        request.requestType = "payWithATM";


//        String data = "partnerCode=" + request.partnerCode
//                + "&accessKey=" + accessKey
//                + "&requestId=" + request.requestId
//                + "&amount=" + request.amount
//                + "&orderId=" + request.orderId
//                + "&orderInfo=" + request.orderInfo
//                + "&returnUrl=" + request.redirectUrl
//                + "&notifyUrl=" + "http://localhost:8080/success/payment"
//                + "&extraData=";
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

        String hashData = MomoEncoderUtils.signHmacSHA256(rawSignature, secretKey);
        request.signature = hashData;

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(request);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://test-payment.momo.vn/v2/gateway/api/create");
        StringEntity stringEntity = new StringEntity(json);
        post.setHeader("content-type", "application/json");
        post.setEntity(stringEntity);

        System.out.println(json);

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> kq = new HashMap<>();

        System.out.println("result --> " + result);

        if (result.get("resultCode").toString().equalsIgnoreCase("0")) {

            kq.put("orderId", result.get("orderId"));
//            kq.put("payUrl", result.get("payUrl"));
//            kq.put("signature", result.get("signature"));
//            kq.put("requestId", result.get("requestId"));
//            kq.put("errorCode", result.get("errorCode"));
//            kq.put("message", result.get("message"));
//            kq.put("localMessage", result.get("localMessage"));
        } else {

            kq.put("orderId", result.get("orderId"));
//            kq.put("signature", result.get("signature"));
//            kq.put("requestId", result.get("requestId"));
//            kq.put("errorCode", result.get("errorCode"));
//            kq.put("message", result.get("message"));
//            kq.put("localMessage", result.get("localMessage"));
        }
        return kq;
    }

    // truy vấn lại trạng thái thanh toán
//    @PostMapping(value = "/transactionStatus")
//    public Map<String, Object> transactionStatus(HttpServletRequest request, @RequestParam String requestId,
//                                                 @RequestParam String orderId)
//            throws InvalidKeyException, NoSuchAlgorithmException, ClientProtocolException, IOException {
//        JSONObject json = new JSONObject();
//        String partnerCode = MomoConfig.PARTNER_CODE;
//        String accessKey = MomoConfig.ACCESS_KEY;
//        String secretKey = MomoConfig.SECRET_KEY;
//        json.put("partnerCode", partnerCode);
//        json.put("accessKey", accessKey);
//        json.put("requestId", requestId);
//        json.put("orderId", orderId);
//        json.put("requestType", "transactionStatus");
//
//        String data = "partnerCode=" + partnerCode + "&accessKey=" + accessKey + "&requestId=" + json.get("requestId")
//                + "&orderId=" + json.get("orderId") + "&requestType=" + json.get("requestType");
//        String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
//        json.put("signature", hashData);
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL);
//        StringEntity stringEntity = new StringEntity(json.toString());
//        post.setHeader("content-type", "application/json");
//        post.setEntity(stringEntity);
//
//        CloseableHttpResponse res = client.execute(post);
//        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
//        StringBuilder resultJsonStr = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            resultJsonStr.append(line);
//        }
//        JSONObject result = new JSONObject(resultJsonStr.toString());
//        Map<String, Object> kq = new HashMap<>();
//        kq.put("requestId", result.get("requestId"));
//        kq.put("orderId", result.get("orderId"));
//        kq.put("extraData", result.get("extraData"));
//        kq.put("amount", Long.parseLong(result.get("amount").toString()));
//        kq.put("transId", result.get("transId"));
//        kq.put("payType", result.get("payType"));
//        kq.put("errorCode", result.get("errorCode"));
//        kq.put("message", result.get("message"));
//        kq.put("localMessage", result.get("localMessage"));
//        kq.put("requestType", result.get("requestType"));
//        kq.put("signature", result.get("signature"));
//        return kq;
//    }

}
