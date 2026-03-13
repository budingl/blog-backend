package com.example.blogbackend.repository;

import com.example.blogbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}