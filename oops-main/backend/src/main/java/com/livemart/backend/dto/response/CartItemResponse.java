package com.livemart.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private String id;
    private String productId;
    private String productName;
    private String productImage;
    private Double productPrice;
    private Integer quantity;
    private Double subtotal;
}
