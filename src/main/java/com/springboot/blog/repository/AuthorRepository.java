package com.springboot.blog.repository;

import com.springboot.blog.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.id in :ids")
    List<Author> findByIds(List<Long> ids);

    @Query(value = "UPDATE Author a SET a.name = :name, a.description = :description WHERE a.id = :id")
    @Modifying
    void updateBatch(Long id, String name, String description);
}
