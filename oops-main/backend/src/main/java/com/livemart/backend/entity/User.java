//user.java
package com.livemart.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CUSTOMER;

    @Column(length = 500)
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "phone_number")
    private String phoneNumber;

    // OAuth provider info
    @Column(name = "provider")
    private String provider; // google, facebook, etc.

    @Column(name = "provider_id")
    private String providerId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "retailer", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "wholesaler", cascade = CascadeType.ALL)
    private List<WholesaleItem> wholesaleItems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "retailer", cascade = CascadeType.ALL)
    private List<RetailerWholesaleOrder> retailerOrders = new ArrayList<>();

    @OneToMany(mappedBy = "wholesaler", cascade = CascadeType.ALL)
    private List<RetailerWholesaleOrder> wholesalerOrders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new ArrayList<>();

    public enum UserRole {
        CUSTOMER,
        RETAILER,
        WHOLESALER,
        ADMIN
    }
}

