package com.livemart.backend.repository;

import com.livemart.backend.entity.RetailerWholesaleOrder;
import com.livemart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetailerWholesaleOrderRepository extends JpaRepository<RetailerWholesaleOrder, String> {
    List<RetailerWholesaleOrder> findByRetailerOrderByCreatedAtDesc(User retailer);
    List<RetailerWholesaleOrder> findByWholesalerOrderByCreatedAtDesc(User wholesaler);
}
