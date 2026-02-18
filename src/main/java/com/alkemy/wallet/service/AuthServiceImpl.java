package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.LoginRequestDto;
import com.alkemy.wallet.dto.request.RegisterRequestDto;
import com.alkemy.wallet.dto.response.JwtAuthenticationResponseDto;
import com.alkemy.wallet.dto.response.UserInfoResponseDto;
import com.alkemy.wallet.entity.Role;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.entity.VerificationToken;
import com.alkemy.wallet.enums.ERole;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.repository.IVerificationTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService{
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IVerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final IJwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, IVerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService, EmailServiceImpl emailService, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.authManager = authManager;
    }
    @Override
    public UserInfoResponseDto registerUser(RegisterRequestDto registerRequest) {
        User newUser = new User();
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setVerified(false);
        Role userRole = roleRepository.findByName(ERole.USER).get();
        newUser.setRole(userRole);
        newUser.setAccounts(null);
        User savedUser = userRepository.save(newUser);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:3000/verify?token=" + token;
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationLink);

        return new UserInfoResponseDto(
                registerRequest.getEmail(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                savedUser.getCreationDate(),
                savedUser.getUpdateDate()
        );
    }

    @Override
    @Transactional
    public void verifyUser(String token) {

        System.out.println("VERIFY ENDPOINT HIT - TOKEN: " + token);
        VerificationToken vToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido"));

        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Token expirado");
        }

        User user = vToken.getUser();
        user.setVerified(true);

        userRepository.save(user);
    }


    @Override
    public void resendVerification(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (Boolean.TRUE.equals(user.getVerified())) {
            throw new RuntimeException("La cuenta ya está verificada");
        }

        VerificationToken token = verificationTokenRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Token no encontrado"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado");
        }

        String verificationLink = "http://localhost:3000/verify?token=" + token.getToken();
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);
    }

    @Override
    public JwtAuthenticationResponseDto loginUser(LoginRequestDto loginRequest) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("Invalid Email or Password"));
        if(user.getSoftDelete() != null && user.getSoftDelete()){
            throw new IllegalArgumentException("Invalid Email or Password");
        }
        if (!Boolean.TRUE.equals(user.getVerified())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes verificar tu cuenta por correo electrónico");
        }
        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                jwt
        );
    }
}
