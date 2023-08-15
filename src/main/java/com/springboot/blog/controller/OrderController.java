package com.springboot.blog.controller;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.User;
import com.springboot.blog.momo.payload.PaymentRequest;
import com.springboot.blog.momo.service.MomoService;
import com.springboot.blog.payload.MessageResponse;
import com.springboot.blog.payload.OrderRequestDto;
import com.springboot.blog.payload.OrderResponseDto;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtAuthenticationFilter;
import com.springboot.blog.security.JwtUtils;
import com.springboot.blog.service.OrderService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(
        name = "CRUD REST APIs for Order Resource"
)
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtTokenFilter;
    private final JwtUtils jwtUtils;
    private final MomoService momoService;

//    @PostMapping
//    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto orderRequest){
//        return ResponseEntity.ok(orderService.placeOrder(orderRequest));
//    }
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(HttpServletRequest httpServletRequest, @RequestBody OrderRequestDto request) {
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User not found with -> username: " + username));

        try {
            var order = orderService.checkOut(user, request);

            if(Objects.equals(request.getPayType(), AppConstants.COD_PAYMENT)){
                orderService.createOrder(order, request.getCheckedCartId());
                return new ResponseEntity<>(order, HttpStatus.OK);
            } else if (
                            Objects.equals(request.getPayType(), AppConstants.MOMO_PAYMENT) ||
                                    Objects.equals(request.getPayType(), AppConstants.MOMO_PAYMENT_ATM) ||
                            Objects.equals(request.getPayType(), AppConstants.MOMO_PAYMENT_CC))
            {

                String payUrl = momoService.createRequest(user.getId(), order, request.getCheckedCartId(), request.getPayType());

                return new ResponseEntity<>(payUrl, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/momo-payment-request")
    public ResponseEntity<?> momoPaymentResponse(@RequestBody PaymentRequest request){
        try {
            momoService.handleResponse(request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(){
        return new ResponseEntity<>("Payment with momo successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> testCreateOrder(@RequestBody Order order, List<Long> checkedCartId) {
//        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
//        String username = jwtUtils.getUserNameFromJwtToken(jwt);
//        User user = userRepository.findByUsername(username).orElseThrow(()
//                -> new UsernameNotFoundException("User not found with -> username: " + username));
//        order.setUser(user);

        orderService.createOrder(order, checkedCartId);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }


    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(HttpServletRequest httpServletRequest) {
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User not found with -> username: " + username));

        List<OrderResponseDto> orderResponseDtos = orderService.getOrders(user);

        return ResponseEntity.ok(orderResponseDtos);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orderResponseDtos = orderService.getAllOrders();

        return ResponseEntity.ok(orderResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

}
