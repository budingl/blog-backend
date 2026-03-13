package com.example.blogbackend.controller;

import com.example.blogbackend.model.Tag;
import com.example.blogbackend.repository.TagRepository;
import com.example.blogbackend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final JwtUtil jwtUtil;

    public TagController(TagRepository tagRepository, JwtUtil jwtUtil) {
        this.tagRepository = tagRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> getTags() {
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tag not found");
        }
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            // 验证 token
            jwtUtil.extractUsername(token.substring(7));

            String name = request.get("name");
            String slug = request.get("slug");

            if (name == null || slug == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name and slug are required");
            }

            Tag tag = new Tag();
            tag.setName(name);
            tag.setSlug(slug);

            tagRepository.save(tag);

            return ResponseEntity.status(HttpStatus.CREATED).body(tag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            // 验证 token
            jwtUtil.extractUsername(token.substring(7));

            Tag tag = tagRepository.findById(id).orElse(null);
            if (tag == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tag not found");
            }

            String name = request.get("name");
            String slug = request.get("slug");

            if (name != null) {
                tag.setName(name);
            }

            if (slug != null) {
                tag.setSlug(slug);
            }

            tagRepository.save(tag);

            return ResponseEntity.ok(tag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            // 验证 token
            jwtUtil.extractUsername(token.substring(7));

            Tag tag = tagRepository.findById(id).orElse(null);
            if (tag == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tag not found");
            }

            tagRepository.delete(tag);

            return ResponseEntity.ok("Tag deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
}
