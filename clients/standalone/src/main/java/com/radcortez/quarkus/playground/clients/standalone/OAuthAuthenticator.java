package com.radcortez.quarkus.playground.clients.standalone;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.Base64;

@Provider
@Priority(Priorities.AUTHENTICATION + 100)
public class OAuthAuthenticator implements ClientRequestFilter {
    @Override
    public void filter(final ClientRequestContext requestContext) {
        final Object clientId = requestContext.getProperty("client_id");
        final Object scope = requestContext.getProperty("scope");

        final TokenStoreClient tokenStoreClient =
            RestClientBuilder.newBuilder()
                             .baseUri(URI.create("http://localhost:8080/"))
                             .build(TokenStoreClient.class);

        final String authorization = createBasicAuthHeaderValue((String) clientId, "password");
        final TokenStoreClient.Token
            token = tokenStoreClient.authenticate(authorization, "client_credentials", (String) scope);

        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    }

    private String createBasicAuthHeaderValue(final String username, final String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
