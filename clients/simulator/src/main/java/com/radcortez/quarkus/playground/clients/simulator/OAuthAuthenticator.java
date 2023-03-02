package com.radcortez.quarkus.playground.clients.simulator;

import com.radcortez.quarkus.playground.clients.simulator.TokenStoreClient.Token;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.Base64;

@Priority(Priorities.AUTHENTICATION + 100)
public class OAuthAuthenticator implements ClientRequestFilter {
    @Inject
    @RestClient
    TokenStoreClient tokenStoreClient;

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final Object clientId = requestContext.getProperty("client_id");
        final Object scope = requestContext.getProperty("scope");

        final String authorization = createBasicAuthHeaderValue((String) clientId, "password");
        final Token token = tokenStoreClient.authenticate(authorization, "client_credentials", (String) scope);

        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    }

    private String createBasicAuthHeaderValue(final String username, final String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
