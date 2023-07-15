package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.CartDto;
import com.springboot.blog.payload.MessageResponse;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtAuthenticationFilter;
import com.springboot.blog.security.JwtUtils;
import com.springboot.blog.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@Tag(
        name = "CRUD REST APIs for Cart Resource"
)
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtTokenFilter;
    private final JwtUtils jwtUtils;

    @Operation(
            summary = "Create Cart REST API",
            description = "Create Cart REST API is used to save product into cart"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 OK"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartDto> addToCart(HttpServletRequest httpServletRequest, @RequestBody CartDto cartDto) {
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User Not Found with -> username: " + username));

        return new ResponseEntity<>(cartService.addToCart(user, cartDto.getProductId(), cartDto.getQuantity()), HttpStatus.OK);

    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CartDto>> getCarts(HttpServletRequest httpServletRequest) {
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User Not Found with -> username: " + username));

        List<CartDto> carts = cartService.getCarts(user.getId());

        return ResponseEntity.ok(carts);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> removeFromCart(HttpServletRequest httpServletRequest, @RequestBody Long productId){
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User Not Found with -> username: " + username));

        cartService.removeFromCart(user.getId(), productId);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> clearCart(HttpServletRequest httpServletRequest) {
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User Not Found with -> username: " + username));

        cartService.clearCart(user.getId());
        return new ResponseEntity<>("Clear cart successfully", HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PostMapping("/batch")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CartDto>> addBatchCart(HttpServletRequest httpServletRequest, List<CartDto> request){
        String jwt = jwtTokenFilter.parseJwt(httpServletRequest);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User Not Found with -> username: " + username));

        List<CartDto> response = cartService.addBatchCart(user, request);

        return ResponseEntity.ok(response);
    }


}
