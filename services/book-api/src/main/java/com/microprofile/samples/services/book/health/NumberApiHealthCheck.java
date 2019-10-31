package com.microprofile.samples.services.book.health;

import com.microprofile.samples.services.book.client.NumberApiClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Readiness
@ApplicationScoped
public class NumberApiHealthCheck implements HealthCheck {
    @Inject
    @RestClient
    NumberApiClient numberApiClient;

    @Override
    public HealthCheckResponse call() {
        final HealthCheckResponseBuilder healthCheck = HealthCheckResponse.named("Number API health check").up();

        try {
            final Response health = numberApiClient.health();
            if (health.getStatus() != Response.Status.OK.getStatusCode()) {
                addWarning(healthCheck);
            }
        } catch (final Exception e) {
            addWarning(healthCheck);
        }

        return healthCheck.build();
    }

    private void addWarning(final HealthCheckResponseBuilder healthCheck) {
        healthCheck.withData("number-api",
                             "Could not reach Number API. The Book Store is unable to retrieve ISBN numbers at " +
                             "this time. This will not affect Books creation. ISBN numbers will be retrieved once" +
                             " Number API is up!");
    }
}
