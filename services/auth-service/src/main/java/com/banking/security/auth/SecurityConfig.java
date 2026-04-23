package com.banking.security.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
// ↑ Critical! Enables WebFlux security
// Gateway uses WebFlux not WebMVC!
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {
        // ↑ ServerHttpSecurity not HttpSecurity!
        // WebFlux version of security config

        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                                // ↑ authorizeExchange not authorizeHttpRequests!
                                // WebFlux equivalent
                                .pathMatchers("/actuator/health").permitAll()
                                .anyExchange().authenticated()
                        // ↑ anyExchange not anyRequest!
                )
                .oauth2Login(Customizer.withDefaults())
                // ↑ Handles login redirect to Keycloak
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()));
        // ↑ Validates JWT tokens

        return http.build();
    }
}