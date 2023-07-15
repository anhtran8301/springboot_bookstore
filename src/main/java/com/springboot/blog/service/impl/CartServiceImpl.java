package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Cart;
import com.springboot.blog.entity.Product;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CartDto;
import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.repository.CartRepository;
import com.springboot.blog.repository.ProductRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtAuthenticationFilter;
import com.springboot.blog.security.JwtUtils;
import com.springboot.blog.service.CartService;
import com.springboot.blog.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtTokenFilter;
    private final JwtUtils jwtUtils;
    private final ModelMapper mapper;
    private final ProductService productService;

    @Override
    public CartDto addToCart(User user, Long productId, int quantity) {

        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId);

        CartDto cartResponse = null;
        if (cart != null) {
            cartResponse = this.updateExistCart(cart, productId, quantity);
        } else {
            cartResponse = this.updateNotExistCart(productId, quantity, user);
        }

        return cartResponse;
    }

    public CartDto updateExistCart(Cart cart, Long productId, int quantity) {
        Product product  = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        cart.setQuantity(cart.getQuantity() + quantity);

        if (cart.getQuantity() <= product.getQuantity() && cart.getQuantity() > 0) {

            Cart updatedCart = cartRepository.save(cart);

//            updatedCart.setProduct(product);

//            10121
//            product.setCart(updatedCart);
            return mapper.map(updatedCart, CartDto.class);
        } else {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Product is not enough");
        }
    }

    public CartDto updateNotExistCart(Long productId, int quantity, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Cart newCart = new Cart(quantity, user, product);

        if (newCart.getQuantity() <= product.getQuantity() && newCart.getQuantity() > 0) {
            var result = cartRepository.save(newCart);
            result.setProduct(product);
            return mapper.map(result, CartDto.class);
        } else {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Product is not enough");
        }

    }

    @Override
    public List<CartDto> addBatchCart(User user, List<CartDto> listCart){

        for (CartDto cartDto : listCart) {
            this.addToCart(user, cartDto.getProductId(), cartDto.getQuantity());
        }

        List<Long> bookIds = listCart.stream()
                .map(CartDto::getProductId)
                .toList();

        List<Cart> result = cartRepository.findAll()
                .stream()
                .filter(x -> bookIds.contains(x.getProduct().getId()) && Objects.equals(x.getUser().getId(), user.getId()))
                .toList();

        List<CartDto> cartResponse = new ArrayList<>();

        for (Cart cart : result) {
            var product = productRepository.findById(cart.getProduct().getId());
            var productResponse = productService.getProductById(product.get().getId());

            cartResponse.add(new CartDto(
                    cart.getId(),
                    cart.getQuantity(),
                    mapper.map(productResponse, ProductDto.class).getId(),
                    user.getId()
            ));
        }
        return cartResponse;
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", productId));

        System.out.println(cart);
            cartRepository.delete(cart);
    }

    @Override
    public List<CartDto> getCarts(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<CartDto> cartResponse = new ArrayList<>();
        for (Cart cart: carts) {
            var product = productRepository.findById(cart.getProduct().getId());
            var productResponse = productService.getProductById(product.get().getId());
            cartResponse.add(new CartDto(
                    cart.getId(),
                    cart.getQuantity(),
//                    mapper.map(productResponse, ProductDto.class).getId(),
                    productResponse.getId(),
                    userId
            ));
        }
        return cartResponse;
    }

    @Override
    public void clearCart(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        if (carts == null || carts.size() == 0) {
            throw new ResourceNotFoundException("Category", "userId", userId);
        } else {
            cartRepository.deleteAll(carts);
        }
    }

    @Override
    public void removeBatchCart(List<Long> cartIds) {
        cartRepository.deleteByIdIn(cartIds);
    }
}
