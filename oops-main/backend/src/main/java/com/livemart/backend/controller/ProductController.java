// ProductController.java (CORRECTED)
package com.livemart.backend.controller;

import com.livemart.backend.dto.request.ProductRequest;
import com.livemart.backend.dto.response.MessageResponse;
import com.livemart.backend.dto.response.ProductResponse;
import com.livemart.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseEntity<?> updateProduct(@PathVariable String id, 
                                          @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable String id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "false") Boolean inStockOnly) {
        List<ProductResponse> response = productService.getAllProducts(categoryId, minPrice, maxPrice, inStockOnly);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseEntity<?> getMyProducts() {
        List<ProductResponse> response = productService.getProductsByRetailer();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
        List<ProductResponse> response = productService.searchProducts(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedProducts() {
        List<ProductResponse> response = productService.getFeaturedProducts();
        return ResponseEntity.ok(response);
    }
}
