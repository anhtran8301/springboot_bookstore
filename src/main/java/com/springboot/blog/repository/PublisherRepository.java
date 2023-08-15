package com.springboot.blog.repository;

import com.springboot.blog.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("SELECT p FROM Publisher p WHERE p.id in :ids")
    List<Publisher> findByIds(List<Long> ids);

    @Query(value = "UPDATE Publisher p SET p.name = :name, p.description = :description, p.imageUrl = :imageUrl WHERE p.id = :id")
    @Modifying
    void updateBatch(Long id, String name, String description, String imageUrl);
}
