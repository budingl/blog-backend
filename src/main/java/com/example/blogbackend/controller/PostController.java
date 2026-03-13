package com.example.blogbackend.controller;

import com.example.blogbackend.model.Category;
import com.example.blogbackend.model.Post;
import com.example.blogbackend.model.Tag;
import com.example.blogbackend.model.User;
import com.example.blogbackend.repository.CategoryRepository;
import com.example.blogbackend.repository.PostRepository;
import com.example.blogbackend.repository.TagRepository;
import com.example.blogbackend.repository.UserRepository;
import com.example.blogbackend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final JwtUtil jwtUtil;

    public PostController(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> getPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getPostsByCategory(@PathVariable Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Post post = new Post();
            post.setTitle((String) request.get("title"));
            post.setContent((String) request.get("content"));
            post.setAuthor(user);
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            // 设置分类
            if (request.get("categoryId") != null) {
                Long categoryId = Long.parseLong(request.get("categoryId").toString());
                Category category = categoryRepository.findById(categoryId).orElse(null);
                if (category != null) {
                    post.setCategory(category);
                }
            }

            // 设置标签
            if (request.get("tagIds") != null) {
                List<Long> tagIds = (List<Long>) request.get("tagIds");
                List<Tag> tags = new ArrayList<>();
                for (Long tagId : tagIds) {
                    Tag tag = tagRepository.findById(tagId).orElse(null);
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                post.setTags(tags);
            }

            postRepository.save(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Post post = postRepository.findById(id).orElse(null);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }

            if (!post.getAuthor().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own posts");
            }

            post.setTitle((String) request.get("title"));
            post.setContent((String) request.get("content"));
            post.setUpdatedAt(LocalDateTime.now());

            // 更新分类
            if (request.get("categoryId") != null) {
                Long categoryId = Long.parseLong(request.get("categoryId").toString());
                Category category = categoryRepository.findById(categoryId).orElse(null);
                post.setCategory(category);
            }

            // 更新标签
            if (request.get("tagIds") != null) {
                List<Long> tagIds = (List<Long>) request.get("tagIds");
                List<Tag> tags = new ArrayList<>();
                for (Long tagId : tagIds) {
                    Tag tag = tagRepository.findById(tagId).orElse(null);
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                post.setTags(tags);
            }

            postRepository.save(post);

            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Post post = postRepository.findById(id).orElse(null);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }

            if (!post.getAuthor().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own posts");
            }

            postRepository.delete(post);

            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
}