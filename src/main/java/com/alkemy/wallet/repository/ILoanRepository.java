package com.alkemy.wallet.repository;

import com.alkemy.wallet.entity.Loan;
import com.alkemy.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByUser(User user);
}
