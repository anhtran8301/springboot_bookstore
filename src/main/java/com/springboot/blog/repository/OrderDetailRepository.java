package com.springboot.blog.repository;

import com.springboot.blog.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> getOrderDetailByOrderId(Long orderId);
}
