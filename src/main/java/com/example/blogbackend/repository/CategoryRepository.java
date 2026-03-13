package com.example.blogbackend.repository;

import com.example.blogbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findBySlug(String slug);
}
