package com.alkemy.wallet.repository;

import com.alkemy.wallet.entity.Role;
import com.alkemy.wallet.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole role);
}
