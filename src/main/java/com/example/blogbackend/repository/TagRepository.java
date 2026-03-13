package com.example.blogbackend.repository;

import com.example.blogbackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findBySlug(String slug);
}
