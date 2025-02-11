package com.keycloak.multiple.service;

import com.keycloak.multiple.repository.TenantRepository;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TenantJwtIssuerValidator implements OAuth2TokenValidator<Jwt> {
    private final TenantRepository tenants;

    private final OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, "The iss claim is not valid",
            "https://tools.ietf.org/html/rfc6750#section-3.1");

    public TenantJwtIssuerValidator(TenantRepository tenants) {
        this.tenants = tenants;
    }


    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
//        if(this.tenants.findById(String.valueOf(token.getIssuer())) != null) {
        return OAuth2TokenValidatorResult.success();
//        }
//        return OAuth2TokenValidatorResult.failure(this.error);
    }
}