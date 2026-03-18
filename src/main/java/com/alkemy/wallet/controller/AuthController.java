package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.request.LoginRequestDto;
import com.alkemy.wallet.dto.request.RegisterRequestDto;
import com.alkemy.wallet.dto.response.JwtAuthenticationResponseDto;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.entity.VerificationToken;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.repository.IVerificationTokenRepository;
import com.alkemy.wallet.service.AuthServiceImpl;
import com.alkemy.wallet.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;
    private final IVerificationTokenRepository verificationTokenRepository;
    private final IUserRepository userRepository;

    public AuthController(AuthServiceImpl authService,
                          IVerificationTokenRepository verificationTokenRepository,
                          IUserRepository userRepository) {
        this.authService = authService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequest){
        JwtAuthenticationResponseDto token = authService.registerUser(registerRequest);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        System.out.println("VERIFY ENDPOINT HIT - TOKEN: " + token);
        VerificationToken vToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }

        if (Boolean.TRUE.equals(vToken.getUsed())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token ya utilizado");
        }

        User user = vToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        vToken.setUsed(true);
        verificationTokenRepository.save(vToken);

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
