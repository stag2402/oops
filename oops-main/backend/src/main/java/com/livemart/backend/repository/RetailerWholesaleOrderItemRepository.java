package com.livemart.backend.repository;

import com.livemart.backend.entity.RetailerWholesaleOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailerWholesaleOrderItemRepository extends JpaRepository<RetailerWholesaleOrderItem, String> {
}
