package com.springboot.blog.repository;

import com.springboot.blog.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByProductIdAndUserId(Long userId, Long productId);

    Cart findByUserIdAndProductId(Long productId, Long userId);

    List<Cart> findByUserId(Long userId);

    void deleteByIdIn(List<Long> cartIds);

    List<Cart> findByIdIn(List<Long> id);

    @Query(value = "SELECT c.product_id FROM carts c WHERE c.id IN :ids", nativeQuery = true)
    List<Long> findProductIdByIdIn(List<Long> ids);
}
