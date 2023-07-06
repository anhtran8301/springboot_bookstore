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
//    public List<StudentDTO> doSearch(StudentDTO studentDTO) {
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("select s.user_id as user_id, code, password, fullname as fullName, email, phone\n" +
//                "from student s join users u on s.user_id = u.id ");
//
//        sql.append(" where 1=1 ");
//
//        if (studentDTO.getKeySearch() != null) {
//            sql.append(" and (Upper(u.fullname) like CONCAT('%',Upper(:name), '%') ESCAPE '&') ");
//        }
//
//        sql.append("order by s.user_id");

    Boolean existsByName(String name);
    List<Product> findByBookCategoryId(Long bookCategoryId);
    //
    Page<Product> findByBookCategoryId(Long bookCategoryId, Pageable pageable);
    //
//    @Query("SELECT p FROM Product p WHERE " +
//            "1 = 1 and " +
//            "p.name LIKE CONCAT('%',:query, '%')" +
//            "Or p.description LIKE CONCAT('%', :query, '%')")
//    List<Product> searchProducts(String query);

//    @Query(value = "SELECT * FROM products p WHERE " +
//            "p.name LIKE CONCAT('%',:query, '%')" +
//            "Or p.description LIKE CONCAT('%', :query, '%')", nativeQuery = true)
//    List<Product> searchProductsSQL(String query);

    //test
    @Query("SELECT p FROM Product p WHERE " +
            "1 = 1 and " +
            "p.name LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')")
    Page<Product> searchProductsPaginate(String query, Pageable pageable);
}
