package com.livemart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // "STRIPE" or "COD"
    
    private String stripePaymentIntentId;
    private String notes;
}
