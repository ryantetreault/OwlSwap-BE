package com.cboard.owlswap.owlswap_backend.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService
{
    public void sendVerificationEmail(String toEmail, String verifyUrl) {
        // DEV VERSION:
        System.out.println("========================================");
        System.out.println("Send verification email to: " + toEmail);
        System.out.println("Verification link: " + verifyUrl);
        System.out.println("========================================");
    }
}
