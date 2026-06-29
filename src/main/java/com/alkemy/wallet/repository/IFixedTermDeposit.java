package com.alkemy.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.alkemy.wallet.entity.FixedTermDeposit;

@Repository
public interface IFixedTermDeposit extends JpaRepository<FixedTermDeposit, Long> {
    
}
