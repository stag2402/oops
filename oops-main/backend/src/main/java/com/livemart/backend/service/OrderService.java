package com.livemart.backend.service;

import com.livemart.backend.dto.request.CreateOrderRequest;
import com.livemart.backend.dto.response.OrderItemResponse;
import com.livemart.backend.dto.response.OrderResponse;
import com.livemart.backend.entity.*;
import com.livemart.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    /**
     * Create order from cart
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = authService.getCurrentUser();

        // Get active cart
        Cart cart = cartRepository.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Check stock availability for all items
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStockStatus() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        }

        // Create order
        String trackingId = "LM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Order order = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .total(cart.getTotalAmount())
                .shippingAddress(request.getShippingAddress())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(request.getPaymentMethod().equals("COD") ? 
                        Order.PaymentStatus.PENDING : Order.PaymentStatus.COMPLETED)
                .orderTrackingId(trackingId)
                .stripePaymentIntentId(request.getStripePaymentIntentId())
                .notes(request.getNotes())
                .build();

        order = orderRepository.save(order);

        // Create order items and update stock
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>(); // <-- Collect updated products

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getDiscountedPrice())
                    .build();
            
            orderItems.add(orderItem);

            // Update stock
            product.setStockStatus(product.getStockStatus() - cartItem.getQuantity());
            productsToUpdate.add(product); // <-- Collect updated product
        }

        orderItemRepository.saveAll(orderItems);
        productRepository.saveAll(productsToUpdate); // <-- Batch save all updated products
        order.setItems(orderItems);

        // Mark cart as completed
        cart.setStatus(Cart.CartStatus.COMPLETED);
        cartRepository.save(cart);

        // Send confirmation email
        emailService.sendOrderConfirmation(user.getEmail(), trackingId, order.getTotal());

        return mapToOrderResponse(order);
    }

    /**
     * Get order by ID
     */
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Verify ownership or retailer access
        User user = authService.getCurrentUser();
        boolean hasAccess = order.getUser().getId().equals(user.getId()) ||
                             order.getItems().stream()
                                   .anyMatch(item -> item.getProduct().getRetailer().getId().equals(user.getId()));
        
        if (!hasAccess) {
            throw new RuntimeException("Unauthorized");
        }

        return mapToOrderResponse(order);
    }

    /**
     * Get all orders for current user
     */
    public List<OrderResponse> getMyOrders() {
        User user = authService.getCurrentUser();
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    /**
     * Update order status (Retailer only)
     */
    @Transactional
    public OrderResponse updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = authService.getCurrentUser();
        
        // Check if user is the retailer of ALL products in the order
        // OR if the user is an ADMIN.
        boolean isSoleRetailer = order.getItems().stream()
                .allMatch(item -> item.getProduct().getRetailer().getId().equals(user.getId()));

        if (!isSoleRetailer && user.getRole() != User.UserRole.ADMIN) {
            throw new RuntimeException("Unauthorized: You can only update the status of orders that contain products exclusively from your retail store.");
        }

        order.setStatus(newStatus);
        
        if (newStatus == Order.OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        order = orderRepository.save(order);

        // Send status update email
        emailService.sendOrderStatusUpdate(order.getUser().getEmail(), 
                order.getOrderTrackingId(), newStatus.name());

        return mapToOrderResponse(order);
    }

    /**
     * Get order by tracking ID
     */
    public OrderResponse getOrderByTrackingId(String trackingId) {
        Order order = orderRepository.findByOrderTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToOrderResponse(order);
    }

    /**
     * Map Order to OrderResponse
     */
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderTrackingId(order.getOrderTrackingId())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .total(order.getTotal())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .items(items)
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .build();
    }

    /**
     * Map OrderItem to OrderItemResponse
     */
    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        Product product = item.getProduct();
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImage(!product.getImages().isEmpty() ? product.getImages().get(0) : null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}


