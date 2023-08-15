package com.springboot.blog.repository;

import com.springboot.blog.entity.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
    List<SaleProduct> findBySaleId(Long saleId);
    SaleProduct findBySaleIdAndProductId(Long saleId, Long productId);
}
