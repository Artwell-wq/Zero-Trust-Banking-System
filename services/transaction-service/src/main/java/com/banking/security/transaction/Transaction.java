package com.banking.security.transaction;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @Column(nullable = false)
    private String fromAccount;

    @Column(nullable = false)
    private String toAccount;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String initiatedBy;
    // ↑ Keycloak username who made the transfer
    //   Critical for DORA audit trail!

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors
    public Transaction() {}

    public Transaction(
            String transactionId,
            String fromAccount,
            String toAccount,
            Double amount,
            String currency,
            String status,
            String initiatedBy) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.initiatedBy = initiatedBy;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
