package com.livemart.backend.dto.response;

import com.livemart.backend.entity.Order.OrderStatus;
import com.livemart.backend.entity.Order.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String orderTrackingId;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Double total;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItemResponse> items;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
}
