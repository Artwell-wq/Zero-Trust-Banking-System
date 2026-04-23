package com.banking.security.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository
        extends JpaRepository<Account, Long> {

    // Find account by username
    // Spring Data JPA generates the SQL automatically!
    Optional<Account> findByUsername(String username);

    // Find all accounts for ADMIN
    List<Account> findAll();
}