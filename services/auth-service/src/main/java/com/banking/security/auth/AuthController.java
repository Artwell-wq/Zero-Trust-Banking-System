package com.banking.security.auth;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        private final AuthService authService;

        public AuthController(AuthService authService) {
            this.authService = authService;
        }

        @PostMapping("/login")
        public Map<String, Object> login(@RequestBody Map<String, String> request) {

            String username = request.get("username");
            String password = request.get("password");

            return authService.login(username, password);
        }
    }

