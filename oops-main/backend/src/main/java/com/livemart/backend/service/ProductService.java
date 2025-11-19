package com.livemart.backend.service;

import com.livemart.backend.dto.request.ProductRequest;
import com.livemart.backend.dto.response.ProductResponse;
import com.livemart.backend.entity.Category;
import com.livemart.backend.entity.Product;
import com.livemart.backend.entity.User;
import com.livemart.backend.repository.CategoryRepository;
import com.livemart.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthService authService;

    /**
     * Create new product (Retailer only)
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        User retailer = authService.getCurrentUser();
        
        if (retailer.getRole() != User.UserRole.RETAILER) {
            throw new RuntimeException("Only retailers can create products");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .images(request.getImages())
                .price(request.getPrice())
                .stockStatus(request.getStockStatus())
                .availabilityDate(request.getAvailabilityDate())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .discountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : 0.0)
                .retailer(retailer)
                .category(category)
                .build();

        product = productRepository.save(product);
        return mapToResponse(product);
    }

    /**
     * Update product
     */
    @Transactional
    public ProductResponse updateProduct(String productId, ProductRequest request) {
        User retailer = authService.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the current user owns this product
        if (!product.getRetailer().getId().equals(retailer.getId())) {
            throw new RuntimeException("You don't have permission to update this product");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImages(request.getImages());
        product.setPrice(request.getPrice());
        product.setStockStatus(request.getStockStatus());
        product.setAvailabilityDate(request.getAvailabilityDate());
        product.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);
        product.setDiscountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : 0.0);
        product.setCategory(category);

        product = productRepository.save(product);
        return mapToResponse(product);
    }

    /**
     * Delete product
     */
    @Transactional
    public void deleteProduct(String productId) {
        User retailer = authService.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getRetailer().getId().equals(retailer.getId())) {
            throw new RuntimeException("You don't have permission to delete this product");
        }

        productRepository.delete(product);
    }

    /**
     * Get product by ID
     */
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    /**
     * Get all products with filters
     */
    public List<ProductResponse> getAllProducts(String categoryId, Double minPrice, Double maxPrice, Boolean inStockOnly) {
        List<Product> products = productRepository.findByFilters(categoryId, minPrice, maxPrice, inStockOnly);
        return products.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Get products by retailer
     */
    public List<ProductResponse> getProductsByRetailer() {
        User retailer = authService.getCurrentUser();
        List<Product> products = productRepository.findByRetailer(retailer);
        return products.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Search products by name
     */
    public List<ProductResponse> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return products.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Get featured products
     */
    public List<ProductResponse> getFeaturedProducts() {
        List<Product> products = productRepository.findByIsFeaturedTrue();
        return products.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Map Product entity to ProductResponse DTO
     */
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .images(product.getImages())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .stockStatus(product.getStockStatus())
                .inStock(product.isInStock())
                .availabilityDate(product.getAvailabilityDate())
                .isFeatured(product.getIsFeatured())
                .discountPercentage(product.getDiscountPercentage())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .retailerId(product.getRetailer().getId())
                .retailerName(product.getRetailer().getName())
                .averageRating(product.getAverageRating())
                .totalReviews(product.getFeedbacks().size())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
