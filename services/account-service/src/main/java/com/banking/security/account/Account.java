package com.banking.security.account;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String username;
    // ↑ Links to Keycloak username
    //   testuser in Keycloak = testuser here

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String status;
    // ACTIVE, SUSPENDED, CLOSED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Account() {}

    public Account(String accountNumber, String username,
                   Double balance, String currency) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
        this.currency = currency;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}