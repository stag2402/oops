package com.livemart.backend.repository;

import com.livemart.backend.entity.Product;
import com.livemart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByRetailer(User retailer);
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:inStockOnly = false OR p.stockStatus > 0)")
    List<Product> findByFilters(
        @Param("categoryId") String categoryId,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("inStockOnly") Boolean inStockOnly
    );
    
    // Find products by nearby retailers
    @Query("SELECT p FROM Product p WHERE p.retailer IN :retailers")
    List<Product> findByRetailers(@Param("retailers") List<User> retailers);
    
    List<Product> findByIsFeaturedTrue();
}
