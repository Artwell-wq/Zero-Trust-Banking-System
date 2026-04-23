package com.banking.security.account;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final WebClient.Builder webClientBuilder;
    private final AccountRepository accountRepository;

    public AccountService(
            WebClient.Builder webClientBuilder,
            AccountRepository accountRepository) {
        this.webClientBuilder = webClientBuilder;
        this.accountRepository = accountRepository;
    }

    // CUSTOMER — get OWN balance from PostgreSQL
    public Map<String, Object> getBalance(String username) {

        Account account = accountRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found for user: "
                                        + username));

        return Map.of(
                "accountNumber", account.getAccountNumber(),
                "owner",         account.getUsername(),
                "balance",       account.getBalance(),
                "currency",      account.getCurrency(),
                "status",        account.getStatus(),
                "securityStatus","VERIFIED_FROM_DATABASE"
        );
    }

    // ADMIN — get ALL accounts from PostgreSQL
    public List<Map<String, Object>> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(account -> Map.<String, Object>of(
                        "accountNumber", account.getAccountNumber(),
                        "owner",         account.getUsername(),
                        "balance",       account.getBalance(),
                        "currency",      account.getCurrency(),
                        "status",        account.getStatus()
                ))
                .collect(Collectors.toList());
    }

    // CUSTOMER — get transaction history
    public List<Map> getTransactionHistory(
            String username, String token) {

        return webClientBuilder
                .build()
                .get()
                .uri("http://localhost:8082/api/transactions/history")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }
}