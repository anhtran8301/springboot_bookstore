package com.springboot.blog.repository;

import com.springboot.blog.entity.Order;
import com.springboot.blog.entity.Transaction;
import com.springboot.blog.entity.composite.OrderPKId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, OrderPKId> {
    List<Transaction> findByOrderId(Long orderId);

    Transaction findFirstByOrderId(Long orderId);
}
