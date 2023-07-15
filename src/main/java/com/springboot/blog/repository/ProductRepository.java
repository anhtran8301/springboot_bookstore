package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
    Page<Product> findByBookCategoryId(Long bookCategoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "1 = 1 and " +
            "p.name LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')")
    Page<Product> searchProductsPaginate(String query, Pageable pageable);

    @Query( "SELECT p FROM Product p WHERE p.id in :ids" )
    List<Product> findByIds(List<Long> ids);

    Product findProductById(Long id);
}
