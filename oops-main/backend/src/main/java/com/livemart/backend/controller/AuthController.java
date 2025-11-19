// AuthController.java
package com.livemart.backend.controller;

import com.livemart.backend.dto.request.LoginRequest;
import com.livemart.backend.dto.request.RegisterRequest;
import com.livemart.backend.dto.request.VerifyOTPRequest;
import com.livemart.backend.dto.response.AuthResponse;
import com.livemart.backend.dto.response.MessageResponse;
import com.livemart.backend.dto.response.UserResponse;
import com.livemart.backend.entity.User;
import com.livemart.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        MessageResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOTPRequest request) {
        AuthResponse response = authService.verifyOTP(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOTP(@RequestParam String email) {
        MessageResponse response = authService.resendOTP(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        User user = authService.getCurrentUser();
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .image(user.getImage())
                .role(user.getRole())
                .address(user.getAddress())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .phoneNumber(user.getPhoneNumber())
                .emailVerified(user.getEmailVerified())
                .build();
        return ResponseEntity.ok(response);
    }
}
