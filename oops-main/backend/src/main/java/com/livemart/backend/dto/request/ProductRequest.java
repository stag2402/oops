package com.livemart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    private List<String> images;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Stock status is required")
    private Integer stockStatus;
    
    private LocalDateTime availabilityDate;
    private Boolean isFeatured;
    private Double discountPercentage;
    
    @NotBlank(message = "Category ID is required")
    private String categoryId;
}
