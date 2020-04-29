package com.microprofile.samples.clients.simulator;

import com.microprofile.samples.clients.simulator.TokenStoreClient.Token;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
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
