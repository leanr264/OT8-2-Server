package com.alkemy.wallet.repository;

import java.util.Optional;

import com.alkemy.wallet.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IVerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
