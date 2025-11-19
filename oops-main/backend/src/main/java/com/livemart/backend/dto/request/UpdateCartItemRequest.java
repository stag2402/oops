package com.livemart.backend.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
