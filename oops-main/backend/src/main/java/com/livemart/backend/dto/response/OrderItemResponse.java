package com.livemart.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private String id;
    private String productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}
