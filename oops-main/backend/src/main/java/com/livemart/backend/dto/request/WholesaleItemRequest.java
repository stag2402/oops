package com.livemart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class WholesaleItemRequest {
    @NotBlank(message = "Item name is required")
    private String name;
    
    private String description;
    private List<String> images;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;
    
    private Integer minimumOrderQuantity;
    
    @NotBlank(message = "Category ID is required")
    private String categoryId;
}
