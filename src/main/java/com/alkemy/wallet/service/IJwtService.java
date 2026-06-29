package com.alkemy.wallet.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface IJwtService {
    String extractUsername(String token);
    String generateToken(Map<String,Object> extraClaims, UserDetails user);
    String generateToken(UserDetails user);
    boolean isTokenValid(String token,UserDetails user);
}
