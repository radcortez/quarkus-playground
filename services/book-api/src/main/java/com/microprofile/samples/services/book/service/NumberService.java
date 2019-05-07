package com.microprofile.samples.services.book.service;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

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


    @Fallback(NumberFallbackHandler.class)
    public String getNumberWithFallback() {
        return getNumber();
    }

    static AtomicInteger retries = new AtomicInteger(0);

    @CircuitBreaker(successThreshold = 2, requestVolumeThreshold = 4, failureRatio = 0.75, delay = 30000)
    @Retry(maxRetries = 5, delay = 1000, jitter = 0, retryOn = RuntimeException.class)
    public String getNumberWithRetry() {
        if (retries.getAndAdd(1) < 5) {
            System.out.println("Try " + retries);
        } else {
            retries.set(0);
        }

        return getNumber();
    }

    @Timeout(value = 500)
    public String getNumberWithTimeout() {
        return getNumber();
    }

    static AtomicInteger executions = new AtomicInteger(0);

    @Bulkhead(value = 3)
    public String getNumberWithBulkhead() {
        final int execution = executions.addAndGet(1);

        try {
            System.out.println("Execution " + execution);
            TimeUnit.SECONDS.sleep(30);
        } catch (final InterruptedException e) {

        }

        return getNumber();
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
