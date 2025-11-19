package com.livemart.backend.repository;

import com.livemart.backend.entity.Cart;
import com.livemart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserAndStatus(User user, Cart.CartStatus status);
    Optional<Cart> findByUserIdAndStatus(String userId, Cart.CartStatus status);
}
