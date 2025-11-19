package com.livemart.backend.repository;

import com.livemart.backend.entity.User;
import com.livemart.backend.entity.WholesaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WholesaleItemRepository extends JpaRepository<WholesaleItem, String> {
    List<WholesaleItem> findByWholesaler(User wholesaler);
    List<WholesaleItem> findByCategoryId(String categoryId);
    List<WholesaleItem> findByNameContainingIgnoreCase(String name);
}
