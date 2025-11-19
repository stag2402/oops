package com.livemart.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private List<String> images;
    private Double price;
    private Double discountedPrice;
    private Integer stockStatus;
    private Boolean inStock;
    private LocalDateTime availabilityDate;
    private Boolean isFeatured;
    private Double discountPercentage;
    private String categoryId;
    private String categoryName;
    private String retailerId;
    private String retailerName;
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
}
