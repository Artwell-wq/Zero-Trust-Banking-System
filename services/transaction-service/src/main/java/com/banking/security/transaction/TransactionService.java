package com.banking.security.transaction;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<String, Object> transfer(
            String fromAccount,
            String toAccount,
            double amount,
            String username) {

        String transactionId = UUID.randomUUID().toString();

        Transaction transaction = new Transaction(
                transactionId,
                fromAccount,
                toAccount,
                amount,
                "EUR",
                "SUCCESS",
                username
        );
        transactionRepository.save(transaction);

        return Map.of(
                "transactionId",  transactionId,
                "fromAccount",    fromAccount,
                "toAccount",      toAccount,
                "amount",         amount,
                "currency",       "EUR",
                "status",         "SUCCESS",
                "timestamp",      transaction.getTimestamp().toString(),
                "initiatedBy",    username,
                "securityStatus", "VERIFIED_STORED_IN_DATABASE"
        );
    }

    public List<Map<String, Object>> getTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(t -> Map.<String, Object>of(
                        "transactionId", t.getTransactionId(),
                        "fromAccount",   t.getFromAccount(),
                        "toAccount",     t.getToAccount(),
                        "amount",        t.getAmount(),
                        "currency",      t.getCurrency(),
                        "status",        t.getStatus(),
                        "initiatedBy",   t.getInitiatedBy(),
                        "timestamp",     t.getTimestamp().toString()
                ))
                .collect(Collectors.toList());
    }

    // ABAC — check if account belongs to user
    public boolean isAccountOwner(
            String username, String accountNumber) {

        Map<String, String> accountOwners = Map.of(
                "IBAN-PL-123456789", "testuser",
                "IBAN-PL-987654321", "adminuser",
                "IBAN-PL-555555555", "john.doe"
        );

        String owner = accountOwners.get(accountNumber);
        return username.equals(owner);
    }
}