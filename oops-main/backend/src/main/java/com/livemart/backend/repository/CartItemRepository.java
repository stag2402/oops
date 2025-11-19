package com.livemart.backend.repository;

import com.livemart.backend.entity.Cart;
import com.livemart.backend.entity.CartItem;
import com.livemart.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
