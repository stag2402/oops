package com.livemart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PlaceWholesaleOrderRequest {
    @NotBlank(message = "Wholesaler ID is required")
    private String wholesalerId;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<WholesaleOrderItemRequest> items;
    
    private String notes;
    
    @Data
    public static class WholesaleOrderItemRequest {
        @NotBlank(message = "Wholesale item ID is required")
        private String wholesaleItemId;
        
        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
