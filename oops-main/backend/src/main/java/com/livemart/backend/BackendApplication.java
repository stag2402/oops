package com.livemart.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("==============================================");
        System.out.println("Live MART Backend API is running!");
        System.out.println("API available at: http://localhost:8080");
        System.out.println("==============================================");
    }
}
