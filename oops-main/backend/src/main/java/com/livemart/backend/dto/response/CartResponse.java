package com.livemart.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {
    private String id;
    private List<CartItemResponse> items;
    private Double totalAmount;
    private Integer totalItems;
}
