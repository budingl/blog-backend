package com.example.blogbackend.controller;

import com.example.blogbackend.model.Category;
import com.example.blogbackend.repository.CategoryRepository;
import com.example.blogbackend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final JwtUtil jwtUtil;

    public CategoryController(CategoryRepository categoryRepository, JwtUtil jwtUtil) {
        this.categoryRepository = categoryRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            // 验证 token
            jwtUtil.extractUsername(token.substring(7));

            String name = request.get("name");
            String slug = request.get("slug");

            if (name == null || slug == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name and slug are required");
            }

            Category category = new Category();
            category.setName(name);
            category.setSlug(slug);

            categoryRepository.save(category);

            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            // 验证 token
            jwtUtil.extractUsername(token.substring(7));

            Category category = categoryRepository.findById(id).orElse(null);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }

            String name = request.get("name");
            String slug = request.get("slug");

            if (name != null) {
                category.setName(name);
            }

            if (slug != null) {
                category.setSlug(slug);
            }

            categoryRepository.save(category);

            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            // 验证 token
            jwtUtil.extractUsername(token.substring(7));

            Category category = categoryRepository.findById(id).orElse(null);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }

            categoryRepository.delete(category);

            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
}
