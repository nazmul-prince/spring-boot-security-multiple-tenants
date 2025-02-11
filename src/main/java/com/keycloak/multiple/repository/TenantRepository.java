package com.keycloak.multiple.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class TenantRepository {

    List<Tenant> tenants = List.of(new Tenant("http://localhost:8070/realms/maintest", "http://localhost:8070/realms/maintest/protocol/openid-connect/certs"),
            new Tenant("http://localhost:8070/realms/test", "http://localhost:8070/realms/test/protocol/openid-connect/certs"));

    public List<Tenant> findById(String tenantId) {
        return tenants.stream()
                .filter(t -> Objects.equals(t.getId(), tenantId))
                .toList();
    }
}