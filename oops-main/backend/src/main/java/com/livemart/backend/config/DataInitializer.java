package com.livemart.backend.config;

import com.livemart.backend.entity.Category;
import com.livemart.backend.entity.User;
import com.livemart.backend.repository.CategoryRepository;
import com.livemart.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing sample data...");

        // Initialize Categories if not exists
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    Category.builder()
                            .name("Electronics")
                            .description("Electronic devices and accessories")
                            .build(),
                    Category.builder()
                            .name("Clothing")
                            .description("Fashion and apparel")
                            .build(),
                    Category.builder()
                            .name("Food & Beverages")
                            .description("Food items and drinks")
                            .build(),
                    Category.builder()
                            .name("Home & Garden")
                            .description("Home improvement and gardening")
                            .build(),
                    Category.builder()
                            .name("Books")
                            .description("Books and educational materials")
                            .build(),
                    Category.builder()
                            .name("Sports & Outdoors")
                            .description("Sports equipment and outdoor gear")
                            .build()
            );
            categoryRepository.saveAll(categories);
            System.out.println("✓ Categories initialized");
        }

        // Initialize Sample Users if not exists
        if (userRepository.count() == 0) {
            // Create a sample customer
            User customer = User.builder()
                    .name("John Doe")
                    .email("customer@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(User.UserRole.CUSTOMER)
                    .emailVerified(true)
                    .address("123 Main St, New York, NY")
                    .latitude(40.7128)
                    .longitude(-74.0060)
                    .phoneNumber("+1234567890")
                    .build();

            // Create a sample retailer
            User retailer = User.builder()
                    .name("ABC Store")
                    .email("retailer@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(User.UserRole.RETAILER)
                    .emailVerified(true)
                    .address("456 Store Ave, New York, NY")
                    .latitude(40.7580)
                    .longitude(-73.9855)
                    .phoneNumber("+1234567891")
                    .build();

            // Create a sample wholesaler
            User wholesaler = User.builder()
                    .name("XYZ Wholesale")
                    .email("wholesaler@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(User.UserRole.WHOLESALER)
                    .emailVerified(true)
                    .address("789 Warehouse Rd, New York, NY")
                    .latitude(40.7489)
                    .longitude(-73.9680)
                    .phoneNumber("+1234567892")
                    .build();

            userRepository.saveAll(Arrays.asList(customer, retailer, wholesaler));
            System.out.println("✓ Sample users initialized");
            System.out.println("  Customer: customer@example.com / password123");
            System.out.println("  Retailer: retailer@example.com / password123");
            System.out.println("  Wholesaler: wholesaler@example.com / password123");
        }

        System.out.println("Sample data initialization complete!");
    }
}
