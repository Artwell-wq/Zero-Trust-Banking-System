package com.banking.security.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuditService {

    // Logger — writes to log file and console
    private static final Logger log =
            LoggerFactory.getLogger(AuditService.class);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Log any action
    public void logAction(
            String action,
            String username,
            String role,
            String ipAddress,
            String status,
            String details) {

        // Structured log format — easy to search in ELK Stack
        log.info("AUDIT" +
                        " | action={}" +
                        " | user={}" +
                        " | role={}" +
                        " | ip={}" +
                        " | timestamp={}" +
                        " | status={}" +
                        " | details={}",
                action,
                username,
                role,
                ipAddress,
                LocalDateTime.now().format(FORMATTER),
                status,
                details
        );
    }

    // Convenience method for successful actions
    public void logSuccess(
            String action,
            String username,
            String role,
            String ipAddress,
            String details) {

        logAction(action, username, role,
                ipAddress, "SUCCESS", details);
    }

    // Convenience method for failed/blocked actions
    public void logBlocked(
            String action,
            String username,
            String role,
            String ipAddress,
            String details) {

        logAction(action, username, role,
                ipAddress, "BLOCKED", details);
    }

    // Convenience method for errors
    public void logError(
            String action,
            String username,
            String role,
            String ipAddress,
            String errorMessage) {

        logAction(action, username, role,
                ipAddress, "ERROR", errorMessage);
    }
}