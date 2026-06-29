package com.alkemy.wallet.repository;

import com.alkemy.wallet.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IInstallmentRepository extends JpaRepository<Installment, Long> {
    List<Installment> findByLoanId(Long loanId);
}
