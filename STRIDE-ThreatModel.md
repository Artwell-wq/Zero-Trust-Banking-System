# STRIDE Threat Model
## Zero-Trust Banking System
### Version 1.0 | April 2026

---

## System Overview
A Zero-Trust, cloud-native banking system
implementing microservices architecture with
centralized IAM (Keycloak), API Gateway,
and PostgreSQL persistence.

---

## Assets to Protect
1. Customer account data (balances, IBAN numbers)
2. Transaction records
3. JWT tokens
4. Keycloak user credentials
5. Service certificates (mTLS)
6. Audit logs

---

## Trust Boundaries
1. Internet → API Gateway (HTTPS)
2. API Gateway → Microservices (mTLS)
3. Microservices → PostgreSQL (private network)
4. Microservices → Keycloak (HTTPS)

---

## STRIDE Analysis

### S — SPOOFING
| Threat | Mitigation | Status |
|--------|-----------|--------|
| Attacker impersonates user | Keycloak MFA + JWT | ✅ |
| Service impersonates another service | mTLS certificates | ✅ |
| Token forgery | RS256 signed JWT | ✅ |
| Brute force login | Keycloak lockout policy | ✅ |

### T — TAMPERING
| Threat | Mitigation | Status |
|--------|-----------|--------|
| Modify data in transit | mTLS + HTTPS | ✅ |
| Modify JWT token | Cryptographic signature | ✅ |
| SQL injection | JPA parameterized queries | ✅ |
| Modify transfer amount | Request validation | ✅ |
| mTLS service identity | Phase 5 cert-manager | ⬜ |

### R — REPUDIATION
| Threat | Mitigation | Status |
|--------|-----------|--------|
| Deny making transfer | initiatedBy + audit log | ✅ |
| Deny accessing account | DORA audit trail | ✅ |
| Deny admin actions | Admin audit logging | ✅ |
| Log tampering | ELK immutable logs | ✅ |

### I — INFORMATION DISCLOSURE
| Threat | Mitigation | Status |
|--------|-----------|--------|
| Access other user's balance | RBAC + JWT username | ✅ |
| Intercept token in transit | HTTPS + mTLS | ✅ |
| Database breach | Private network | ✅ |
| Expose internal service URLs | Gateway hides services | ✅ |

### D — DENIAL OF SERVICE
| Threat | Mitigation | Status |
|--------|-----------|--------|
| Flood Gateway with requests | Rate limiting (TODO) | ⬜ |
| Service crash | Docker auto-restart | ✅ |
| Database overload | Connection pooling | ✅ |
| Multiple instances needed | Kubernetes (Phase 5) | ⬜ |

### E — ELEVATION OF PRIVILEGE
| Threat | Mitigation | Status |
|--------|-----------|--------|
| CUSTOMER accesses ADMIN endpoint | hasAuthority RBAC | ✅ |
| Bypass Gateway to hit service directly | mTLS client-auth | ✅ |
| Token with fake roles | Keycloak signs tokens | ✅ |
| High value transfer without SCA | PSD3 Step-Up Auth | ✅ |

---

## Risk Summary
| Category | Threats | Mitigated | Remaining |
|----------|---------|-----------|-----------|
| Spoofing | 4 | 4 | 0 |
| Tampering | 4 | 4 | 0 |
| Repudiation | 4 | 4 | 0 |
| Info Disclosure | 4 | 4 | 0 |
| Denial of Service | 4 | 2 | 2 |
| Elevation of Privilege | 4 | 4 | 0 |
| **TOTAL** | **24** | **22** | **2** |

---

## Remaining Risks
1. Rate limiting not yet implemented
2. Single instance — no redundancy (Phase 5)

---

## Regulatory Alignment
- DORA: Repudiation threats mitigated via audit logging
- PSD3: Spoofing threats mitigated via SCA/MFA
- Zero Trust: All 6 STRIDE categories addressed