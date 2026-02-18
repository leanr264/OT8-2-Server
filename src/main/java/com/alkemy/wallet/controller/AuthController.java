package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.request.LoginRequestDto;
import com.alkemy.wallet.dto.request.RegisterRequestDto;
import com.alkemy.wallet.dto.response.JwtAuthenticationResponseDto;
import com.alkemy.wallet.dto.response.UserInfoResponseDto;
import com.alkemy.wallet.service.AuthServiceImpl;
import com.alkemy.wallet.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequest){
        UserInfoResponseDto response = authService.registerUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        authService.verifyUser(token);
        return new ResponseEntity<>("¡Cuenta verificada correctamente!", HttpStatus.OK);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam String email) {
        authService.resendVerification(email);
        return ResponseEntity.ok("Correo reenviado correctamente");
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequest){
        JwtAuthenticationResponseDto token = authService.loginUser(loginRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
