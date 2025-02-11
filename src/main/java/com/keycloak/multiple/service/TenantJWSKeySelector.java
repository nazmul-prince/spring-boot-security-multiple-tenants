package com.keycloak.multiple.service;

import com.keycloak.multiple.repository.Tenant;
import com.keycloak.multiple.repository.TenantRepository;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.JWTClaimsSetAwareJWSKeySelector;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class TenantJWSKeySelector
        implements JWTClaimsSetAwareJWSKeySelector<SecurityContext> {

    private final TenantRepository tenants;
    private final Map<String, JWSKeySelector<SecurityContext>> selectors = new ConcurrentHashMap<>();

    public TenantJWSKeySelector(TenantRepository tenants) {
        this.tenants = tenants;
    }

    @Override
    public List<? extends Key> selectKeys(JWSHeader jwsHeader, JWTClaimsSet jwtClaimsSet, SecurityContext securityContext)
            throws KeySourceException {
        System.out.println("Selecting keys");
        return this.selectors.computeIfAbsent(toTenant(jwtClaimsSet), new Function<String, JWSKeySelector<SecurityContext>>() {
                    @Override
                    public JWSKeySelector<SecurityContext> apply(String tenant) {
                        return TenantJWSKeySelector.this.fromTenant(tenant);
                    }
                })
                .selectJWSKeys(jwsHeader, securityContext);
    }

    private String toTenant(JWTClaimsSet claimSet) {
        return (String) claimSet.getClaim("iss");
    }

    private JWSKeySelector<SecurityContext> fromTenant(String tenant) {
        final var securityContextJWSKeySelector = this.tenants.findById(tenant)
                .stream()
                .map(new Function<Tenant, String>() {
                    @Override
                    public String apply(Tenant t) {
                        return t.getJwksUri();
                    }
                })
                .map(new Function<String, JWSKeySelector<SecurityContext>>() {
                    @Override
                    public JWSKeySelector<SecurityContext> apply(String uri) {
                        return TenantJWSKeySelector.this.fromUri(uri);
                    }
                })
                .findFirst()
                .get();
        return securityContextJWSKeySelector;
    }

    private JWSKeySelector<SecurityContext> fromUri(String uri) {
        try {
            return JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(new URL(uri));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}