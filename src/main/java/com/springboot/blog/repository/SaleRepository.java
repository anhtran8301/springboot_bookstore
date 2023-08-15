package com.springboot.blog.repository;

import com.springboot.blog.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s WHERE s.startDate <= current_date AND current_date <= s.endDate")
    List<Sale> getCurrentSales();

    @Query("SELECT s FROM Sale s WHERE s.startDate <= :startDate AND s.endDate >= :endDate")
    Sale sameTimeSale(LocalDateTime startDate, LocalDateTime endDate);
}
