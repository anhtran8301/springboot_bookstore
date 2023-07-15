package com.springboot.blog.controller;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.MessageResponse;
import com.springboot.blog.payload.OrderRequestDto;
import com.springboot.blog.payload.OrderResponseDto;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtAuthenticationFilter;
import com.springboot.blog.security.JwtUtils;
import com.springboot.blog.service.OrderService;
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

//    @PostMapping
//    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto orderRequest){
//        return ResponseEntity.ok(orderService.placeOrder(orderRequest));
//    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(HttpServletRequest httpServletRequest, @RequestBody OrderRequestDto orderRequestDto) {
        try {
            String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User user = userRepository.findByUsername(username).orElseThrow(()
                    -> new UsernameNotFoundException("User not found with -> username: " + username));
            return new ResponseEntity<>(orderService.checkOut(user, orderRequestDto), HttpStatus.OK);

        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order){
//        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
//        String username = jwtUtils.getUserNameFromJwtToken(jwt);
//        User user = userRepository.findByUsername(username).orElseThrow(()
//                -> new UsernameNotFoundException("User not found with -> username: " + username));
//        order.setUser(user);

        orderService.createOrder(order);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }


    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(HttpServletRequest httpServletRequest){
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
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        List<OrderResponseDto> orderResponseDtos = orderService.getAllOrders();

        return ResponseEntity.ok(orderResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }
}
