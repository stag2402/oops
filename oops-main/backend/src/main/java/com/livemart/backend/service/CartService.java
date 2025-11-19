package com.livemart.backend.service;

import com.livemart.backend.dto.request.AddToCartRequest;
import com.livemart.backend.dto.response.CartItemResponse;
import com.livemart.backend.dto.response.CartResponse;
import com.livemart.backend.entity.*;
import com.livemart.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthService authService;

    /**
     * Get or create active cart for current user
     */
    private Cart getOrCreateCart() {
        User user = authService.getCurrentUser();
        return cartRepository.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .user(user)
                            .status(Cart.CartStatus.ACTIVE)
                            .build();
                    return cartRepository.save(cart);
                });
    }

    /**
     * Add item to cart
     */
    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCart();
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product is in stock
        if (product.getStockStatus() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Check if item already exists in cart
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            // Update quantity
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            // Create new cart item
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
        }

        cartItemRepository.save(cartItem);
        return getCart();
    }

    /**
     * Get current user's cart
     */
    public CartResponse getCart() {
        Cart cart = getOrCreateCart();
        
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());

        return CartResponse.builder()
                .id(cart.getId())
                .items(items)
                .totalAmount(cart.getTotalAmount())
                .totalItems(cart.getTotalItems())
                .build();
    }

    /**
     * Update cart item quantity
     */
    @Transactional
    public CartResponse updateCartItem(String cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Verify ownership
        User user = authService.getCurrentUser();
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // Check stock
        if (cartItem.getProduct().getStockStatus() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        
        return getCart();
    }

    /**
     * Remove item from cart
     */
    @Transactional
    public CartResponse removeFromCart(String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Verify ownership
        User user = authService.getCurrentUser();
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(cartItem);
        return getCart();
    }

    /**
     * Clear cart
     */
    @Transactional
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    /**
     * Map CartItem to CartItemResponse
     */
    private CartItemResponse mapToCartItemResponse(CartItem item) {
        Product product = item.getProduct();
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImage(!product.getImages().isEmpty() ? product.getImages().get(0) : null)
                .productPrice(product.getDiscountedPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
}
