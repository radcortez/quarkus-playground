package com.microprofile.samples.clients.simulator;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Priority(Priorities.AUTHENTICATION + 100 - 1)
public class OAuthCredentials implements ClientRequestFilter {
    private final String username;
    private final String password;
    private final String grantType;
    private final String scope;

    public OAuthCredentials() {
        this.username = "naruto";
        this.password = "";
        this.grantType = "client_credentials";
        this.scope = "admin";
    }

    private OAuthCredentials(final String username, final String password, final String grantType, final String scope) {
        this.username = username;
        this.password = password;
        this.grantType = grantType;
        this.scope = scope;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        requestContext.setProperty("client_id", username);
        requestContext.setProperty("client_secret", password);
        requestContext.setProperty("grant_type", grantType);
        requestContext.setProperty("scope", scope);
    }

    static OAuthCredentials oauthCredentials(
        final String username,
        final String password,
        final String grantType,
        final String scope) {
        return new OAuthCredentials(username, password, grantType, scope);
    }
}
