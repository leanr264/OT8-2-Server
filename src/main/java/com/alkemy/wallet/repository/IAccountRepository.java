package com.alkemy.wallet.repository;

import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.enums.ECurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByUserAndCurrency(User user, ECurrency currency);
}
