package com.alkemy.wallet.repository;

import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByAccountIn(List<Account> accounts, Pageable pageable);
}
