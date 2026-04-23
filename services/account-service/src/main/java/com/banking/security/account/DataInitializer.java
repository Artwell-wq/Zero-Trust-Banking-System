package com.banking.security.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Only create data if database is empty
        if (accountRepository.count() == 0) {

            // Create testuser account
            accountRepository.save(new Account(
                    "IBAN-PL-123456789",
                    "testuser",
                    5000.00,
                    "EUR"
            ));

            // Create adminuser account
            accountRepository.save(new Account(
                    "IBAN-PL-987654321",
                    "adminuser",
                    15000.00,
                    "EUR"
            ));

            // Create john.doe account
            accountRepository.save(new Account(
                    "IBAN-PL-555555555",
                    "john.doe",
                    2500.00,
                    "EUR"
            ));

            System.out.println("✅ Test accounts created in PostgreSQL!");
        } else {
            System.out.println("✅ Accounts already exist in PostgreSQL!");
        }
    }
}