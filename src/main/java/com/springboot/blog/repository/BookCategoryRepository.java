package com.springboot.blog.repository;


import com.springboot.blog.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    Boolean existsByName(String name);
}
