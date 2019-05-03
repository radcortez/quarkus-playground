package com.microprofile.samples.services.book.service;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class NumberService {
    @Inject
    @ConfigProperty(name = "NUMBER_TARGET_URL", defaultValue = "http://localhost:8081/number-api/numbers/generate")
    private String numberApiTargetUrl;

    private Client client;
    private WebTarget numberApi;

    @PostConstruct
    private void init() {
        client = ClientTracingRegistrar.configure(ClientBuilder.newBuilder()).build();
        numberApi = client.target(numberApiTargetUrl);
    }

    @PreDestroy
    private void destroy() {
        client.close();
    }

    @CircuitBreaker
    @Fallback(NumberFallbackHandler.class)
    public String getNumber() {
        final Response response = numberApi
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token())
                .get();

        if (OK.getStatusCode() == response.getStatus()) {
            return response.readEntity(String.class);
        }

        throw new WebApplicationException(INTERNAL_SERVER_ERROR);
    }

    // generate a token locally instead of calling the IDP to get a real token.
    private String token() {
        try {
            final JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            final JSONObject jwtContent = (JSONObject) parser.parse(String.format("" +
                    "{\n" +
                    "  \"iss\": \"/oauth2/token\",\n" +
                    "  \"jti\": \"a-123\",\n" +
                    "  \"sub\": \"24400320\",\n" +
                    "  \"username\": \"Alex\",\n" +
                    "  \"upn\": \"Alex\",\n" +
                    "  \"email\": \"alex@example.com\",\n" +
                    "  \"preferred_username\": \"alex\",\n" +
                    "  \"aud\": \"s6BhdRkqt3\",\n" +
                    "  \"exp\": %s,\n" +
                    "  \"iat\": %s,\n" +
                    "  \"token-type\": \"access-token\",\n" +
                    "  \"groups\": [\n" +
                    "    \"create\",\n" +
                    "    \"update\",\n" +
                    "    \"delete\",\n" +
                    "    \"number-api\"\n" +
                    "  ]\n" +
                    "}", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30), System.currentTimeMillis()));
            return TokenUtil.generateTokenString(jwtContent, null, new HashMap<>());

        } catch (final Exception e) {
            return ""; // definitely not what we want in real world because it's going to fail
        }
    }
}
