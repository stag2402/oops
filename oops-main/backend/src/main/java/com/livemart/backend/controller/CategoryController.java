// CategoryController.java
package com.livemart.backend.controller;

import com.livemart.backend.dto.response.CategoryResponse;
import com.livemart.backend.dto.response.MessageResponse;
import com.livemart.backend.entity.Category;
import com.livemart.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryResponse> response = categories.stream()
                    .map(cat -> CategoryResponse.builder()
                            .id(cat.getId())
                            .name(cat.getName())
                            .description(cat.getDescription())
                            .imageUrl(cat.getImageUrl())
                            .productCount(cat.getProducts().size())
                            .build())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable String id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            
            CategoryResponse response = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .imageUrl(category.getImageUrl())
                    .productCount(category.getProducts().size())
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
