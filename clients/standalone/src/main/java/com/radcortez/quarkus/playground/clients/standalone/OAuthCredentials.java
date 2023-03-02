package com.radcortez.quarkus.playground.clients.standalone;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION + 100 - 1)
public class OAuthCredentials implements ClientRequestFilter {
    private final String username;
    private final String password;
    private final String grantType;
    private final String scope;

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
