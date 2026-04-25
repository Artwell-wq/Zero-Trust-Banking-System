package com.banking.security.transaction;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AuditService auditService;

    public TransactionController(
            TransactionService transactionService,
            AuditService auditService) {
        this.transactionService = transactionService;
        this.auditService = auditService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(
            @RequestBody Map<String, Object> request,
            Authentication authentication,
            HttpServletRequest httpRequest) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String ipAddress = httpRequest.getRemoteAddr();

        String fromAccount = (String) request.get("fromAccount");
        String toAccount = (String) request.get("toAccount");
        double amount = Double.parseDouble(
                request.get("amount").toString());

        // ABAC Check 1 — fromAccount must belong to user
        if (!transactionService.isAccountOwner(username, fromAccount)) {

            auditService.logBlocked(
                    "UNAUTHORIZED_TRANSFER_ATTEMPT",
                    username, "CUSTOMER", ipAddress,
                    "User attempted to transfer from account " +
                            fromAccount + " which does not belong to them!"
            );

            return ResponseEntity.status(403).body(Map.of(
                    "error",   "ABAC_VIOLATION",
                    "message", "You can only transfer from your own account!",
                    "abac",    "Attribute: fromAccount must belong to authenticated user"
            ));
        }

        // ABAC Check 2 — Cannot transfer to yourself
        if (fromAccount.equals(toAccount)) {

            auditService.logBlocked(
                    "SELF_TRANSFER_BLOCKED",
                    username, "CUSTOMER", ipAddress,
                    "User attempted to transfer to own account"
            );

            return ResponseEntity.status(400).body(Map.of(
                    "error",   "INVALID_TRANSFER",
                    "message", "Cannot transfer to your own account!"
            ));
        }

        // ABAC Check 3 — Amount must be positive
        if (amount <= 0) {
            return ResponseEntity.status(400).body(Map.of(
                    "error",   "INVALID_AMOUNT",
                    "message", "Transfer amount must be greater than zero!"
            ));
        }

        // PSD3 SCA Check
        if (amount > 100) {
            String scaToken = (String) request.get("scaConfirmed");
            if (scaToken == null || !scaToken.equals("CONFIRMED")) {

                auditService.logBlocked(
                        "HIGH_VALUE_TRANSFER_BLOCKED",
                        username, "CUSTOMER", ipAddress,
                        "Transfer of " + amount + " EUR. SCA required!"
                );

                return ResponseEntity.status(403).body(Map.of(
                        "error",          "SCA_REQUIRED",
                        "message",        "High value transfer detected! Amount: "
                                + amount + " EUR.",
                        "action",         "Please include scaConfirmed: CONFIRMED",
                        "psd3Compliance", "PSD3 Article 97 — Strong Customer Authentication"
                ));
            }
        }

        auditService.logSuccess(
                "TRANSFER_INITIATED", username, "CUSTOMER", ipAddress,
                "Transfer of " + amount + " EUR" +
                        " from " + fromAccount + " to " + toAccount +
                        (amount > 100 ? " (SCA verified)" : " (under limit)")
        );

        Map<String, Object> result =
                transactionService.transfer(
                        fromAccount, toAccount, amount, username);

        auditService.logSuccess(
                "TRANSFER_COMPLETED", username, "CUSTOMER", ipAddress,
                "Transaction " + result.get("transactionId") +
                        " stored in database"
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public List<Map<String, Object>> getTransactions(
            Authentication authentication,
            HttpServletRequest httpRequest) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String ipAddress = httpRequest.getRemoteAddr();

        auditService.logSuccess(
                "TRANSACTION_HISTORY_VIEWED",
                username, "CUSTOMER", ipAddress,
                "Transaction history accessed from database"
        );

        return transactionService.getTransactions();
    }
}