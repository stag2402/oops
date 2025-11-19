package com.livemart.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOTP(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Live MART - Email Verification OTP");
            message.setText("Your OTP for email verification is: " + otp + "\n\nThis OTP will expire in 10 minutes.\n\nThank you for registering with Live MART!");
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendOrderConfirmation(String toEmail, String orderTrackingId, Double total) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Live MART - Order Confirmation");
            message.setText("Your order has been placed successfully!\n\n" +
                    "Order Tracking ID: " + orderTrackingId + "\n" +
                    "Total Amount: $" + total + "\n\n" +
                    "Thank you for shopping with Live MART!");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
        }
    }

    public void sendOrderStatusUpdate(String toEmail, String orderTrackingId, String status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Live MART - Order Status Update");
            message.setText("Your order status has been updated!\n\n" +
                    "Order Tracking ID: " + orderTrackingId + "\n" +
                    "New Status: " + status + "\n\n" +
                    "Thank you for shopping with Live MART!");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send order status update email: " + e.getMessage());
        }
    }
}
