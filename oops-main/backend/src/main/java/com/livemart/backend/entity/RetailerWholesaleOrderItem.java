// RetailerWholesaleOrderItem.java
package com.livemart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retailer_wholesale_order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetailerWholesaleOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private RetailerWholesaleOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wholesale_item_id", nullable = false)
    private WholesaleItem wholesaleItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    public Double getSubtotal() {
        return price * quantity;
    }
}
