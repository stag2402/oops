package com.livemart.backend.config;

import com.livemart.backend.repository.OTPTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class OTPCleanupScheduler {

    @Autowired
    private OTPTokenRepository otpTokenRepository;

    // Run every hour to clean up expired OTPs
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupExpiredOTPs() {
        otpTokenRepository.deleteByExpiresBefore(LocalDateTime.now());
        System.out.println("Cleaned up expired OTP tokens");
    }
}
