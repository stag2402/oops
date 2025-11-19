// OrderController.java
package com.livemart.backend.controller;

import com.livemart.backend.dto.request.CreateOrderRequest;
import com.livemart.backend.dto.request.UpdateOrderStatusRequest;
import com.livemart.backend.dto.response.MessageResponse;
import com.livemart.backend.dto.response.OrderResponse;
import com.livemart.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            OrderResponse response = orderService.createOrder(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders() {
        try {
            List<OrderResponse> response = orderService.getMyOrders();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        try {
            OrderResponse response = orderService.getOrderById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/tracking/{trackingId}")
    public ResponseEntity<?> getOrderByTrackingId(@PathVariable String trackingId) {
        try {
            OrderResponse response = orderService.getOrderByTrackingId(trackingId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RETAILER', 'ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String id, 
                                               @Valid @RequestBody UpdateOrderStatusRequest request) {
        try {
            OrderResponse response = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
