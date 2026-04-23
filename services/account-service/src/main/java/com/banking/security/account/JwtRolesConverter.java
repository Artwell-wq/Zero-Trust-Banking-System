

package com.banking.security.account;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

    public class JwtRolesConverter implements
            Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {

            // Extract realm_access.roles from JWT
            Map<String, Object> realmAccess =
                    jwt.getClaimAsMap("realm_access");
            //                         ↑
            // This is where Keycloak puts realm roles:
            // realm_access: { roles: ["CUSTOMER", "ADMIN"] }

            if (realmAccess == null) {
                return Collections.emptyList();
            }

            List<String> roles =
                    (List<String>) realmAccess.get("roles");
            //                                   ↑
            // Extract the roles list

            if (roles == null) {
                return Collections.emptyList();
            }

            // Convert each role string to GrantedAuthority
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    //             ↑
                    // "CUSTOMER" → GrantedAuthority("CUSTOMER")
                    .collect(Collectors.toList());
        }
    }

