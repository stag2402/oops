// CartController.java
package com.livemart.backend.controller;

import com.livemart.backend.dto.request.AddToCartRequest;
import com.livemart.backend.dto.request.UpdateCartItemRequest;
import com.livemart.backend.dto.response.CartResponse;
import com.livemart.backend.dto.response.MessageResponse;
import com.livemart.backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartResponse response = cartService.addToCart(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getCart() {
        CartResponse response = cartService.getCart();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable String itemId, 
                                           @Valid @RequestBody UpdateCartItemRequest request) {
        CartResponse response = cartService.updateCartItem(itemId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String itemId) {
        CartResponse response = cartService.removeFromCart(itemId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(new MessageResponse("Cart cleared successfully"));
    }
}
