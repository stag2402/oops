package com.livemart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartRequest {
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    @Positive(message = "Quantity must be positive")
    private Integer quantity = 1;
}
