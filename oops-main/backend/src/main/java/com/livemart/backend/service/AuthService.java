package com.livemart.backend.service;

import com.livemart.backend.dto.request.LoginRequest;
import com.livemart.backend.dto.request.RegisterRequest;
import com.livemart.backend.dto.request.VerifyOTPRequest;
import com.livemart.backend.dto.response.AuthResponse;
import com.livemart.backend.dto.response.MessageResponse;
import com.livemart.backend.entity.OTPToken;
import com.livemart.backend.entity.User;
import com.livemart.backend.repository.OTPTokenRepository;
import com.livemart.backend.repository.UserRepository;
import com.livemart.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPTokenRepository otpTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    /**
     * Register a new user and send OTP
     */
    @Transactional
    public MessageResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .phoneNumber(request.getPhoneNumber())
                .emailVerified(false)
                .build();

        userRepository.save(user);

        // Generate and send OTP
        String otp = generateOTP();
        saveOTP(request.getEmail(), otp);
        emailService.sendOTP(request.getEmail(), otp);

        return new MessageResponse("Registration successful. Please verify your email with the OTP sent.");
    }

    /**
     * Verify OTP and activate account
     */
    @Transactional
    public AuthResponse verifyOTP(VerifyOTPRequest request) {
        // Find OTP token
        OTPToken otpToken = otpTokenRepository.findByEmailAndToken(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        // Check if expired
        if (otpToken.getExpires().isBefore(LocalDateTime.now())) {
            otpTokenRepository.delete(otpToken);
            throw new RuntimeException("OTP has expired");
        }

        // Verify user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEmailVerified(true);
        userRepository.save(user);

        // Delete used OTP
        otpTokenRepository.deleteByEmail(request.getEmail());

        // Generate JWT token
        String token = tokenProvider.generateTokenFromEmail(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if email is verified
        if (!user.getEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email first.");
        }

        // Generate JWT token
        String token = tokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    /**
     * Google login (simplified - you'll need to implement Google token verification)
     */
    @Transactional
    public AuthResponse googleLogin(String idToken) {
        // TODO: Verify Google ID token using Google API
        // For now, this is a placeholder
        // You would typically:
        // 1. Verify the token with Google
        // 2. Extract user info (email, name, picture)
        // 3. Create or update user
        // 4. Generate JWT token

        throw new RuntimeException("Google login not yet implemented");
    }

    /**
     * Resend OTP
     */
    @Transactional
    public MessageResponse resendOTP(String email) {
        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        // Delete old OTPs
        otpTokenRepository.deleteByEmail(email);

        // Generate and send new OTP
        String otp = generateOTP();
        saveOTP(email, otp);
        emailService.sendOTP(email, otp);

        return new MessageResponse("OTP resent successfully");
    }

    /**
     * Generate 6-digit OTP
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Save OTP token
     */
    private void saveOTP(String email, String otp) {
        OTPToken otpToken = OTPToken.builder()
                .email(email)
                .token(otp)
                .expires(LocalDateTime.now().plusMinutes(10)) // Valid for 10 minutes
                .build();

        otpTokenRepository.save(otpToken);
    }

    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
