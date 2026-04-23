package com.banking.security.auth;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

    @Service
    public class AuthService {

        public Map<String, Object> login(String username, String password) {


            if ("admin".equals(username) && "password".equals(password)) {

                return Map.of(
                        "accessToken", UUID.randomUUID().toString(),
                        "tokenType", "Bearer",
                        "expiresIn", 3600,
                        "securityStatus", "MOCK_AUTH_SUCCESS"
                );
            }

            return Map.of(
                    "error", "Invalid credentials",
                    "securityStatus", "AUTH_FAILED"
            );
        }
    }

