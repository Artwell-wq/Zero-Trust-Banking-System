package com.banking.security.account;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AuditService auditService;
    // ↑ Inject AuditService

    public AccountController(
            AccountService accountService,
            AuditService auditService) {
        this.accountService = accountService;
        this.auditService = auditService;
    }

    @GetMapping("/my-balance")
    public Map<String, Object> getBalance(
            Authentication authentication,
            HttpServletRequest request) {
        // ↑ Spring injects this automatically
        //   Contains IP address and request info

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String ipAddress = request.getRemoteAddr();
        // ↑ Extract IP address of caller

        // Log the action for DORA compliance
        auditService.logSuccess(
                "BALANCE_VIEWED",
                username,
                "CUSTOMER",
                ipAddress,
                "Account balance accessed"
        );

        return accountService.getBalance(username);
    }

    @GetMapping("/my-transactions")
    public List<Map> getMyTransactions(
            Authentication authentication,
            HttpServletRequest request) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String token = jwt.getTokenValue();
        String ipAddress = request.getRemoteAddr();

        // Log for DORA
        auditService.logSuccess(
                "TRANSACTIONS_VIEWED",
                username,
                "CUSTOMER",
                ipAddress,
                "Transaction history accessed"
        );

        return accountService.getTransactionHistory(username, token);
    }

    @GetMapping("/all-accounts")
    public List<Map<String, Object>> getAllAccounts(
            Authentication authentication,
            HttpServletRequest request) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String ipAddress = request.getRemoteAddr();

        // ADMIN action — very important to log!
        auditService.logSuccess(
                "ALL_ACCOUNTS_VIEWED",
                username,
                "ADMIN",
                ipAddress,
                "Admin accessed all accounts — " +
                        "sensitive operation"
        );

        return accountService.getAllAccounts();
    }
}