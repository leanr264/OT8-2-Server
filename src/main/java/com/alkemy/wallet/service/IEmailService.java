package com.alkemy.wallet.service;

public interface IEmailService {
    public void sendVerificationEmail(String toEmail, String verificationLink);
}
