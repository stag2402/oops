package com.livemart.backend.repository;

import com.livemart.backend.entity.Feedback;
import com.livemart.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByProductOrderByCreatedAtDesc(Product product);
    List<Feedback> findByProductIdOrderByCreatedAtDesc(String productId);
}
