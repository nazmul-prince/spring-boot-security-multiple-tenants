package com.keycloak.multiple.repository;

public class Tenant {
    private String id;
    private String jwksUri;

    public Tenant(String id, String jwksUri) {
        this.id = id;
        this.jwksUri = jwksUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }
}
