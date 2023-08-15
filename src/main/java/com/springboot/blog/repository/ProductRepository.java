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
    Boolean existsBySlug(String slug);
    Page<Product> findByBookCategoryId(Long bookCategoryId, Pageable pageable);
    Page<Product> findByAuthorId(Long authorId, Pageable pageable);
    Page<Product> findByPublisherId(Long publisherId, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE " +
            "1 = 1 and " +
            "p.name LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')")
    Page<Product> searchProductsPaginate(String query, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id in :ids")
    List<Product> findByIds(List<Long> ids);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 ORDER BY p.sold DESC")
    Page<Product> findBestSeller(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.dateCreated <= current_date ORDER BY p.dateCreated DESC")
    Page<Product> findNewest(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.discount > 0 ORDER BY p.discount DESC")
    Page<Product> findBestDiscount(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.releaseDate > current_date ORDER BY p.discount DESC")
    Page<Product> findUpComing(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.bookCategory.id = :bookCategoryId AND p.id != :id ORDER BY p.sold DESC")
    Page<Product> findRelatedBooks(Pageable pageable, Long id, Long bookCategoryId);

    Product findProductById(Long id);

    @Query("SELECT sp.product.id, " +
            "sp.discount, sp.quantity, " +
            "s.startDate, s.endDate, " +
            "s.isActive " +
            "FROM Sale s LEFT JOIN SaleProduct sp ON s.id = sp.sale.id")
    List<?> joinSaleAndSaleProduct();

//    @Query("SELECT sp.product.id, " +
//            "sp.discount, sp.quantity, " +
//            "s.startDate, s.endDate, " +
//            "s.isActive " +
//            "FROM Sale s LEFT JOIN SaleProduct sp ON s.id = sp.sale.id")
//    List<?> joinSaleAndSaleProduct();

}
