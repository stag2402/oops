package com.livemart.backend.repository;

import com.livemart.backend.entity.Order;
import com.livemart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Order> findByOrderTrackingId(String orderTrackingId);
    List<Order> findByStatus(Order.OrderStatus status);
}
